package com.task1.Task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;

    @Column(name = "tag")
    private String tag;
    @Column(name = "content")
    private String content;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "fk_post_id",referencedColumnName = "id")
    @JsonIgnore
    private User user;




//    @UpdateTimestamp
//    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name = "author_id")
//    private User author;

}
