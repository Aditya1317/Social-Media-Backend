package com.task1.Task.service;


import com.task1.Task.dto.Profiledto;


public interface ProfileService {
    Profiledto insertProfileDetails(Long id, Profiledto profiledto);

    Profiledto getProfileById(Long id);

    Profiledto followAnotherId(Long id,Long userId);

    Profiledto unfollowAnotherId(Long id);
}
