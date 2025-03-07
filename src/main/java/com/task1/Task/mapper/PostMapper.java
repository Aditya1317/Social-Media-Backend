package com.task1.Task.mapper;

import com.task1.Task.dto.Postdto;
import com.task1.Task.dto.Userdto;
import com.task1.Task.entity.Post;
import com.task1.Task.entity.User;

public class PostMapper {



    public static Postdto mapToTaskdto(Post post){

        return new Postdto(post.getId(),
                post.getTitle(),
                post.getAuthor(),
                post.getTag(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUser());
    }

    public static Post mapToTask(Postdto postdto){
        return new Post(postdto.getId(),
                postdto.getTitle(),
                postdto.getAuthor(),
                postdto.getTag(),
                postdto.getContent(),
                postdto.getCreatedAt(),
                postdto.getUser());
    }


}
