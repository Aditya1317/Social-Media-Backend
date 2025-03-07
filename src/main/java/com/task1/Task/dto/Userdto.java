package com.task1.Task.dto;

import com.task1.Task.entity.Post;
import com.task1.Task.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Userdto {
    private Long id;

    private String name;

    private String email;

    private String contactNo;

    private LocalDateTime date;

    private Profile profile;

    private String password;

//    private List<Post> posts;

//    private LocalDateTime updatedAt;
}
