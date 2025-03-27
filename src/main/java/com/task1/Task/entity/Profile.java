package com.task1.Task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long followers;
    private Long following;
    private Long noOfPosts;
    private String bio;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @JsonBackReference
    @JsonIgnore
    private User user;
}
