package com.task1.Task.controller;

import com.task1.Task.dto.Postdto;
import com.task1.Task.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;



//    To Create a Post
//    @PostMapping("/posts")
//    public ResponseEntity<Postdto> createPost(@RequestBody Postdto postdto){
//        Postdto savedPost= postService.createPost(postdto);
//        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
//    }

    @PostMapping("/create/{id}")
    public ResponseEntity<Postdto> createPost(@PathVariable("id") Long postId, @RequestBody Postdto postDto) {
        return new ResponseEntity<>(postService.createPost(postId, postDto), HttpStatus.CREATED);
    }

//    Create multiple post

    public ResponseEntity<List<Postdto>> createMultiplePost(@RequestBody List<Postdto> postdtos){

        List<Postdto> resultPost=postService.createAllPost(postdtos);
        return ResponseEntity.ok(resultPost);
    }



//    TO Find a post by ID
    @GetMapping("{id}")
    public ResponseEntity<Postdto> findbyId(@PathVariable("id") Long id){
        Postdto postdto = postService.findbyId(id);
        return ResponseEntity.ok(postdto);
    }

//    TO get all the posts
    @GetMapping("/posts")
    public ResponseEntity<List<Postdto>> getallPost(){

        List<Postdto> tasks= postService.getallPost();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/posts/{id}")
    public ResponseEntity<Postdto> updatePost(@PathVariable("id") Long id,@RequestBody Postdto postdto){
        Postdto postdto1= postService.updatePost(id, postdto);
        return ResponseEntity.ok(postdto1);
    }


    @DeleteMapping("/posts/{id}")
    public String deletebyId(@PathVariable("id") Long id){
        return postService.deletebyId(id);
    }


    @GetMapping("/filter/tag/{tag}")
    public ResponseEntity<List<Postdto>> filterbyTag(@PathVariable("tag") String tag){
        List<Postdto> postdto=postService.filterbyTag(tag);
        return ResponseEntity.ok(postdto);
    }

    @GetMapping("/filter/author/{author}")
    public ResponseEntity<List<Postdto>> filterbyAuthor(@PathVariable("author") String author){
        List<Postdto> postdto=postService.filterbyAuthor(author);
        return ResponseEntity.ok(postdto);
    }





}
