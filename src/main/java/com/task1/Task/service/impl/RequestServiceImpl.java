package com.task1.Task.service.impl;

import com.task1.Task.dto.Profiledto;
import com.task1.Task.entity.FollowerRequest;
import com.task1.Task.entity.Profile;
import com.task1.Task.exception.ResourceNotFoundException;
import com.task1.Task.repository.FollowerRequestRepository;
import com.task1.Task.repository.ProfileRepository;
import com.task1.Task.repository.UserRepository;
import com.task1.Task.service.RequestService;
import org.flowable.task.api.Task;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RequestServiceImpl implements RequestService {



    @Autowired
    private UserRepository socialUserRepository;


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    private final ProfileRepository profileRepository;
    private final FollowerRequestRepository followerRequestRepository;
    private final ModelMapper modelMapper;
    private static final String PROCESS_DEFINITION_KEY = "SocialMedia";

    public RequestServiceImpl(ProfileRepository profileRepository, FollowerRequestRepository followerRequestRepository, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.followerRequestRepository = followerRequestRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String followAnotherId(Long profileId, Long loggedInUserId) {
        if (Objects.equals(profileId, loggedInUserId)) {
            throw new ResourceNotFoundException("You cannot send a follow request to yourself");
        }

        // Retrieve user profiles
        Profile followerProfile = profileRepository.findByUserId(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("Logged-in User not found"));

        Profile followedProfile = profileRepository.findByUserId(profileId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        // Check if a follow request already exists
        Optional<FollowerRequest> existingRequest = followerRequestRepository
                .findByRequestByUserIdAndRequestToUserId(loggedInUserId, profileId);

        if (existingRequest.isPresent()) {
            String status = existingRequest.get().getStatus();

            if ("PENDING".equals(status) || "ACCEPTED".equals(status)) {
                throw new ResourceNotFoundException("Follow request already exists in " + status + " state");
            }

            if ("UNFOLLOWED".equals(status) || "REJECTED".equals(status)) {
                // Update existing request instead of creating a new one
                existingRequest.get().setStatus("PENDING");
                followerRequestRepository.save(existingRequest.get());

                return "Follow request re-sent successfully!";
            }
        }

        // Create a new follow request
        FollowerRequest newRequest = new FollowerRequest();
        newRequest.setRequestToUserId(profileId);
        newRequest.setRequestByUserId(loggedInUserId);
        newRequest.setStatus("PENDING");

        followerRequestRepository.save(newRequest);

        // Start a Flowable Process
        Map<String, Object> variables = new HashMap<>();
        variables.put("requestById", loggedInUserId);
        variables.put("requestToId", profileId);
        variables.put("requestId", newRequest.getId());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

        return "Follow request sent. Process Instance ID: " + processInstance.getId();
    }

    @Override
    public String acceptFollowRequest(Long requestId, Long loggedInUserId, String flowableProcessId) {

        // Fetch the active task for the given process instance ID
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(flowableProcessId)
                .active()
                .list();

        if (tasks.isEmpty()) {
            return "No active follow request task found for approval.";
        }

        Task task = tasks.get(0); // Assuming only one active task per process instance

        Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
        Long requestById = (Long) variables.get("requestById");
        Long requestToId = (Long) variables.get("requestToId");

        // Ensure the task is associated with the correct request
        if (!requestById.equals(requestId) || !requestToId.equals(loggedInUserId)) {
            return "Task request data does not match the provided follow request.";
        }

        // Complete the task with updated variables
        Map<String, Object> completeVariables = new HashMap<>();
        completeVariables.put("requestStatus", "ACCEPTED");
        taskService.complete(task.getId(), completeVariables);

        // Find the follow request based on requestByUserId and requestToUserId
        FollowerRequest followRequest = followerRequestRepository
                .findByRequestByUserIdAndRequestToUserId(requestId, loggedInUserId)
                .orElseThrow(() -> new RuntimeException("Follow request not found"));

        // Validate that the request belongs to the logged-in user
        if (!followRequest.getRequestToUserId().equals(loggedInUserId)) {
            throw new RuntimeException("You are not authorized to accept this request");
        }

        // Update the status to "ACCEPTED"
        followRequest.setStatus("ACCEPTED");
        followerRequestRepository.save(followRequest);

        // Update follower & following counts
        Profile followerProfile = profileRepository.findByUserId(followRequest.getRequestByUserId())
                .orElseThrow(() -> new RuntimeException("Follower profile not found"));
        Profile followedProfile = profileRepository.findByUserId(followRequest.getRequestToUserId())
                .orElseThrow(() -> new RuntimeException("Followed profile not found"));


        // Increase the following count for the logged-in user
        if (followerProfile.getFollowing() == null) {
            followerProfile.setFollowing(0L);
        }
        followerProfile.setFollowing(followerProfile.getFollowing() + 1);

        // Increase the followers count for the person being followed
        if (followedProfile.getFollowers() == null) {
            followedProfile.setFollowers(0L);
        }
        followedProfile.setFollowers(followedProfile.getFollowers() + 1);



        // Save the updated profiles
        profileRepository.save(followerProfile);
        profileRepository.save(followedProfile);



        return "Follow request accepted and process task completed.";
    }


    @Override
    public String rejectFollowRequest(Long requestId, Long loggedInUserId, String flowableProcessId) {
        // Find the follow request based on requestByUserId and requestToUserId
        FollowerRequest followRequest = followerRequestRepository
                .findByRequestByUserIdAndRequestToUserId(requestId, loggedInUserId)
                .orElseThrow(() -> new RuntimeException("Follow request not found"));

        // Validate that the request belongs to the logged-in user
        if (!followRequest.getRequestToUserId().equals(loggedInUserId)) {
            throw new RuntimeException("You are not authorized to reject this request");
        }

        // Update the status to "REJECTED"
        followRequest.setStatus("REJECTED");
        followerRequestRepository.save(followRequest);

        // Fetch the active task for the given process instance ID
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(flowableProcessId)
                .active()
                .list();

        if (tasks.isEmpty()) {
            return "No active follow request task found for rejection.";
        }

        Task task = tasks.get(0); // Assuming only one active task per process instance

        // Retrieve process variables
        Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
        Long requestById = (Long) variables.get("requestById");
        Long requestToId = (Long) variables.get("requestToId");

        // Ensure the task is associated with the correct request
        if (!requestById.equals(requestId) || !requestToId.equals(loggedInUserId)) {
            return "Task request data does not match the provided follow request.";
        }

        // Complete the task with updated variables
        Map<String, Object> completeVariables = new HashMap<>();
        completeVariables.put("requestStatus", "REJECTED");
        taskService.complete(task.getId(), completeVariables);

        return "Follow request rejected and process task completed.";
    }



    @Override
    public Profiledto unfollowAnotherId(Long requestUserId, Long loggedInUserId) {
        // Check if the follow request exists and is in "ACCEPTED" status
        FollowerRequest followRequest = followerRequestRepository
                .findByRequestByUserIdAndRequestToUserId(loggedInUserId, requestUserId)
                .orElseThrow(() -> new RuntimeException("Follow request not found"));

        if (!"ACCEPTED".equals(followRequest.getStatus())) {
            throw new RuntimeException("You are not following this user");
        }

        //  follow request mark as "UNFOLLOWED"
        followRequest.setStatus("UNFOLLOWED");
        followerRequestRepository.save(followRequest);


        // Fetch the profiles of both users
        Profile followerProfile = profileRepository.findByUserId(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("Your profile not found"));
        Profile followedProfile = profileRepository.findByUserId(requestUserId)
                .orElseThrow(() -> new RuntimeException("Followed profile not found"));

        // Ensure non-null follower/following counts before decrementing
        if (followerProfile.getFollowing() == null) followerProfile.setFollowing(0L);
        if (followedProfile.getFollowers() == null) followedProfile.setFollowers(0L);

        // Decrease the following & follower count (ensure it doesn't go negative)
        followerProfile.setFollowing(Math.max(0, followerProfile.getFollowing() - 1));
        followedProfile.setFollowers(Math.max(0, followedProfile.getFollowers() - 1));

        // Save the updated profiles
        profileRepository.save(followerProfile);
        profileRepository.save(followedProfile);

        return modelMapper.map(followerProfile, Profiledto.class);
    }

//    @Override
//    public List<FollowerRequest> getPendingFollowerRequests(Long userId) {
//        return followerRequestRepository.findByRequestToUserIdAndStatus(userId, "PENDING");
//    }
}
