package com.task1.Task.repository;

import com.task1.Task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByName(String name);

//    Optional<User> findByName(String name);
    @Query("SELECT u FROM User u WHERE u.id NOT IN " +
            "(SELECT DISTINCT p.user.id FROM Post p WHERE p.createdAt >= :thirtyDaysAgo)")
    List<User> findInactiveUsers(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

}
