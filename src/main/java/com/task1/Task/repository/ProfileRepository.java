package com.task1.Task.repository;

import com.task1.Task.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Optional<Profile> findByUserId(Long loggedInUserId);

//    List<Profile> findByUserId(Long loggedInUserId);
}

