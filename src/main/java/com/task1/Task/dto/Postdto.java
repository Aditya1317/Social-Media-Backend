package com.task1.Task.dto;

import com.task1.Task.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Postdto {
    private Long id;

    private String title;

    private String author;

    private String tag;

    private String content;

    private LocalDateTime createdAt;

    private User user;




//    private LocalDateTime updatedAt;
}
