package com.task1.Task.dto;

import com.task1.Task.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profiledto {
    private Long id;

    private Long followers;

    private Long following;

    private Long noOfPosts;

    private String bio;

}
