package com.task1.Task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String email;
    String contactNo;
    String password;
    @CreationTimestamp
    LocalDateTime date;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonManagedReference
    @JsonIgnore
    private Profile profile;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_post_id",referencedColumnName = "id")

    private List<Post> posts;
}
