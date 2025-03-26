package com.task1.Task.service;


import com.task1.Task.dto.Profiledto;
import com.task1.Task.entity.FollowerRequest;

import java.util.List;


public interface ProfileService {
    Profiledto insertProfileDetails(Long id, Profiledto profiledto);

    Profiledto getProfileById(Long id);

    Profiledto followAnotherId(Long id,Long userId);

    Profiledto unfollowAnotherId(Long requestUserId, Long loggedInUserId);

    Profiledto acceptFollowRequest(Long requestId, Long loggedInUserId);

    List<FollowerRequest> getPendingFollowerRequests(Long userId);

    Profiledto rejectFollowRequest(Long requestId, Long loggedInUserId);
}
