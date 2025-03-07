package com.task1.Task.service.impl;

import com.task1.Task.dto.Postdto;
import com.task1.Task.entity.Post;
import com.task1.Task.entity.User;
import com.task1.Task.exception.ResourceNotFoundException;
import com.task1.Task.mapper.PostMapper;
import com.task1.Task.repository.PostRepository;
import com.task1.Task.repository.UserRepository;
import com.task1.Task.service.PostService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

//    @Override
//    public Postdto createPost(Postdto postdto) {
//        Post post = PostMapper.mapToTask(postdto);
//        Post savedPost = postRepository.save(post);
//
//        return PostMapper.mapToTaskdto(savedPost);
//    }

    public Postdto createPost(Long userId, Postdto postDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));

        if (user.getProfile().getNoOfPosts() == null) {
            user.getProfile().setNoOfPosts(0L);
        }

        user.getProfile().setNoOfPosts(user.getProfile().getNoOfPosts() + 1);

        Post post = modelMapper.map(postDto, Post.class);
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return modelMapper.map(savedPost, Postdto.class);
    }

    @Override
    public Postdto findbyId(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow( ()-> new RuntimeException("Post with Id "+id+" does not exist"));


        return PostMapper.mapToTaskdto(post);
    }

    @Override
    public List<Postdto> getallPost() {
        List<Post> posts=postRepository.findAll();
        List<Postdto> postdtos=new ArrayList<>();

        for (Post post:posts){
            postdtos.add(PostMapper.mapToTaskdto(post));
        }

        return postdtos;

    }

    @Override
    public Postdto updatePost(Long id,Postdto postdto) {
        Post post=postRepository.findById(id).orElseThrow(()-> new RuntimeException("Post not found with ID:"+id));

        post.setAuthor(postdto.getAuthor());
        post.setContent(postdto.getContent());
        post.setTag(postdto.getTag());
        post.setTitle(postdto.getTitle());

        Post savedPost = postRepository.save(post);

        return PostMapper.mapToTaskdto(savedPost);
    }

    @Override
    public String deletebyId(Long id) {
        Post post=postRepository.findById(id).orElseThrow(()-> new RuntimeException("Post not found with ID:"+id));
       postRepository.deleteById(post.getId());
        return "Data Deleted for the ID:"+post.getId();
    }

    @Override
    public List<Postdto> filterbyTag(String tag) {
        List<Post> posts=postRepository.filterbyTag(tag);
        List<Postdto> postdtos=new ArrayList<>();

        for(Post post:posts){
            postdtos.add(PostMapper.mapToTaskdto(post));
        }

        return postdtos;
    }

    @Override
    public List<Postdto> filterbyAuthor(String author) {
        List<Post> post=postRepository.filterbyAuthor(author.toLowerCase());
        List<Postdto> postdtos=new ArrayList<>();

        for (Post post1:post){
            postdtos.add(PostMapper.mapToTaskdto(post1));
        }
        return postdtos;

    }

    @Override
    public List<Postdto> createAllPost(List<Postdto> postdtos) {

        List<Post> posts=new ArrayList<>();
        for(Postdto postdto:postdtos){
            posts.add(PostMapper.mapToTask(postdto));
        }

        List<Post>posts1= postRepository.saveAll(posts);


        return null;
    }
}
