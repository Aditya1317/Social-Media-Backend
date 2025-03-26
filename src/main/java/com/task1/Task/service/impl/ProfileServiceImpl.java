package com.task1.Task.service.impl;

import com.task1.Task.dto.Profiledto;
import com.task1.Task.entity.FollowerRequest;
import com.task1.Task.entity.Profile;
import com.task1.Task.exception.ResourceNotFoundException;
import com.task1.Task.repository.FollowerRequestRepository;
import com.task1.Task.repository.ProfileRepository;
import com.task1.Task.service.ProfileService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    FollowerRequestRepository followerRequestRepository;

    @Override
    public Profiledto insertProfileDetails(Long id, Profiledto profiledto) {
        Profile profile= profileRepository.findById(id).orElseThrow( ()->new RuntimeException("No id found"));


        profile.setBio(profiledto.getBio());
        Profile result=profileRepository.save(profile);
        return modelMapper.map(result,Profiledto.class);
    }

    @Override
    public Profiledto getProfileById(Long id) {
        Profile profile=profileRepository.findById(id).orElseThrow( ()->new RuntimeException("Not found") );
        return modelMapper.map(profile,Profiledto.class);


    }

//    @Override
//    public Profiledto followAnotherId(Long id) {
//        Profile profile=profileRepository.findById(id).orElseThrow( ()->new RuntimeException("Not found") );
//        if (profile.getFollowing() == null) {
//            profile.setFollowing(0L); // Initialize if null
//        }
//
//
//        profile.setFollowing(profile.getFollowing()+1);
//        Profile result=profileRepository.save(profile);
//        return modelMapper.map(result,Profiledto.class);
//    }


    @Override
    public List<FollowerRequest> getPendingFollowerRequests(Long userId) {
        return followerRequestRepository.findByRequestToUserIdAndStatus(userId, "PENDING");
    }

//    New----------------------------------
@Override
public Profiledto followAnotherId(Long profileId, Long loggedInUserId) {

    if (Objects.equals(profileId, loggedInUserId)) {
        throw new ResourceNotFoundException("You cannot send a follow request to yourself");
    }
    // Retrieve the profile of the logged-in user
    Profile followerProfile = profileRepository.findByUserId(loggedInUserId)
            .orElseThrow(() -> new RuntimeException("Logged-in User not found"));

    // Retrieve the profile of the user being followed
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
        }

    } else {
        // Create a new follow request
        FollowerRequest newRequest = new FollowerRequest();
        newRequest.setRequestToUserId(profileId);
        newRequest.setRequestByUserId(loggedInUserId);
        newRequest.setStatus("PENDING");

        followerRequestRepository.save(newRequest);
    }

    return modelMapper.map(followedProfile, Profiledto.class);
}


//-------------------------------------------------
//OLD
//    @Override
//    public Profiledto followAnotherId(Long profileId ,Long loggedInUserId ) {
//        // Retrieve the profile of the logged-in user
////        System.out.println("0");
//        Profile followerProfile = profileRepository.findByUserId(loggedInUserId)
//                .orElseThrow(() -> new RuntimeException("Logged-in User not found"));
//////        System.out.println("1");
////
////        // Retrieve the profile of the user being followed
//        Profile followedProfile = profileRepository.findByUserId(profileId)
//                .orElseThrow(() -> new RuntimeException("User to follow not found"));
////
////        // Increase the following count for the logged-in user
//        if (followerProfile.getFollowing() == null) {
//            followerProfile.setFollowing(0L);
//        }
////        followerProfile.setFollowing(followerProfile.getFollowing() + 1);
//
//        // Increase the followers count for the person being followed
//        if (followedProfile.getFollowers() == null) {
//            followedProfile.setFollowers(0L);
//        }
////        followedProfile.setFollowers(followedProfile.getFollowers() + 1);
////
////        // Save the updates to both profiles
////        profileRepository.save(followerProfile);
////        profileRepository.save(followedProfile);
//
//
//        //follwerRequest changes
//        FollowerRequest followerRequest = new FollowerRequest();
//        followerRequest.setRequestToUserId(profileId);
//        followerRequest.setRequestByUserId(loggedInUserId);
//        followerRequest.setStatus("PENDING");
//
//        followerRequestRepository.save(followerRequest);
//
//        // Return the followed user's updated profile as DTO
//        return modelMapper.map(followedProfile, Profiledto.class);
//    }

    @Override
    public Profiledto acceptFollowRequest(Long requestId, Long loggedInUserId) {
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

        // Increment following & followers count
        followerProfile.setFollowing(followerProfile.getFollowing() + 1);
        followedProfile.setFollowers(followedProfile.getFollowers() + 1);

        // Save the updated profiles
        profileRepository.save(followerProfile);
        profileRepository.save(followedProfile);

        return modelMapper.map(followedProfile, Profiledto.class);
    }


//    @Override
//    public Profiledto acceptFollowRequest(Long requestId, Long loggedInUserId) {
//        // Find the follow request
//        FollowerRequest followRequest = followerRequestRepository.findByIdAndRequestToUserId(requestId, loggedInUserId)
//                .orElseThrow(() -> new RuntimeException("Follow request not found"));
//
////         Validate that the request belongs to the logged-in user
//        if (!followRequest.getRequestToUserId().equals(loggedInUserId)) {
//            throw new RuntimeException("You are not authorized to accept this request");
//        }
//
//        // Update the status to "ACCEPTED"
//        followRequest.setStatus("ACCEPTED");
//        followerRequestRepository.save(followRequest);
//
//        // Update follower & following counts
//        Profile followerProfile = profileRepository.findByUserId(followRequest.getRequestByUserId())
//                .orElseThrow(() -> new RuntimeException("Follower profile not found"));
//        Profile followedProfile = profileRepository.findByUserId(followRequest.getRequestToUserId())
//                .orElseThrow(() -> new RuntimeException("Followed profile not found"));
//
//        // Increment following & followers count
//        followerProfile.setFollowing(followerProfile.getFollowing() + 1);
//        followedProfile.setFollowers(followedProfile.getFollowers() + 1);
//
//        // Save the updated profiles
//        profileRepository.save(followerProfile);
//        profileRepository.save(followedProfile);
//
//        return modelMapper.map(followedProfile, Profiledto.class);
//    }




@Override
public Profiledto rejectFollowRequest(Long requestId, Long loggedInUserId) {
    // Find the follow request based on requestByUserId and requestToUserId
    FollowerRequest followRequest = followerRequestRepository
            .findByRequestByUserIdAndRequestToUserId(requestId, loggedInUserId)
            .orElseThrow(() -> new RuntimeException("Follow request not found"));

    // Validate that the request belongs to the logged-in user
    if (!followRequest.getRequestToUserId().equals(loggedInUserId)) {
        throw new RuntimeException("You are not authorized to reject this request");
    }

    // Update the status to "ACCEPTED"
    followRequest.setStatus("REJECTED");
    followerRequestRepository.save(followRequest);

    // Update follower & following counts
    Profile followerProfile = profileRepository.findByUserId(followRequest.getRequestByUserId())
            .orElseThrow(() -> new RuntimeException("Follower profile not found"));
    Profile followedProfile = profileRepository.findByUserId(followRequest.getRequestToUserId())
            .orElseThrow(() -> new RuntimeException("Followed profile not found"));


    return modelMapper.map(followedProfile, Profiledto.class);
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



}
