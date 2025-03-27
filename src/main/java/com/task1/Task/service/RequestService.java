package com.task1.Task.service;

import com.task1.Task.dto.Profiledto;
import com.task1.Task.entity.FollowerRequest;

import java.util.List;

public interface RequestService {
    String followAnotherId(Long profileId, Long loggedInUserId);

    String acceptFollowRequest(Long requestId, Long userId,String flowableProcessId);
//    Profiledto rejectFollowRequest(Long requestId, Long loggedInUserId);
    String rejectFollowRequest(Long requestId, Long userId,String flowableProcessId);
    Profiledto unfollowAnotherId(Long requestUserId, Long loggedInUserId);
//    List<FollowerRequest> getPendingFollowerRequests(Long userId);
}