package com.task1.Task.service.impl;

import com.task1.Task.dto.Profiledto;
import com.task1.Task.entity.Profile;
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

    @Override
    public Profiledto followAnotherId(Long id) {
        Profile profile=profileRepository.findById(id).orElseThrow( ()->new RuntimeException("Not found") );
        if (profile.getFollowing() == null) {
            profile.setFollowing(0L); // Initialize if null
        }


        profile.setFollowing(profile.getFollowing()+1);
        Profile result=profileRepository.save(profile);
        return modelMapper.map(result,Profiledto.class);
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
