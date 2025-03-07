package com.task1.Task.service;

import com.task1.Task.dto.Postdto;

import java.util.List;

public interface PostService {

    Postdto createPost(Long userId,Postdto postdto);
    Postdto findbyId(Long id);
    List<Postdto> getallPost();
    Postdto updatePost(Long id,Postdto postdto);
    String deletebyId(Long id);
    List<Postdto> filterbyTag(String tag);
    List<Postdto> filterbyAuthor(String author);

    List<Postdto> createAllPost(List<Postdto> postdtos);
}
