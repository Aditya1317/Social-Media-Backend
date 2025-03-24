package com.task1.Task.service.impl;

import com.task1.Task.dto.Profiledto;
import com.task1.Task.entity.FollowerRequest;
import com.task1.Task.entity.Profile;
import com.task1.Task.repository.FollowerRequestRepository;
import com.task1.Task.repository.ProfileRepository;
import com.task1.Task.service.ProfileService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Profiledto followAnotherId(Long profileId ,Long loggedInUserId ) {
        // Retrieve the profile of the logged-in user
        System.out.println("0");
        Profile followerProfile = profileRepository.findByUserId(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("Logged-in User not found"));
        System.out.println("1");

        // Retrieve the profile of the user being followed
        Profile followedProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

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

        // Save the updates to both profiles
        profileRepository.save(followerProfile);
        profileRepository.save(followedProfile);


        //follwerRequest changes
        FollowerRequest followerRequest = new FollowerRequest();
        followerRequest.setRequest_to_user_id(profileId);
        followerRequest.setRequest_by_user_id(loggedInUserId);
        followerRequest.setStatus("PENDING");

        followerRequestRepository.save(followerRequest);

        // Return the followed user's updated profile as DTO
        return modelMapper.map(followedProfile, Profiledto.class);
    }


    @Override
    public Profiledto unfollowAnotherId(Long id) {
        Profile profile=profileRepository.findById(id).orElseThrow( ()->new RuntimeException("Not found") );
        if (profile.getFollowing() == null) {
            profile.setFollowing(0L); // Initialize if null
        }

        profile.setFollowing(profile.getFollowing()-1);
        Profile result=profileRepository.save(profile);
        return modelMapper.map(result,Profiledto.class);
    }


}
