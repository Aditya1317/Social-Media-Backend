package com.task1.Task.repository;

import com.task1.Task.entity.FollowerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowerRequestRepository extends JpaRepository<FollowerRequest,Long> {

//    Optional<FollowerRequest> findById(Long id);
 Optional<FollowerRequest> findByRequestByUserId(Long requestByUserId);
    List<FollowerRequest> findByRequestToUserIdAndStatus(Long requestToUserId, String status);

    @Query("SELECT f FROM FollowerRequest f " +
            "WHERE f.requestToUserId = :requestToUserId " +
            "AND f.requestByUserId = :requestByUserId")
    Optional<FollowerRequest> findByRequestByUserIdAndRequestToUserId(
            @Param("requestByUserId") Long requestByUserId,
            @Param("requestToUserId") Long requestToUserId);

}
