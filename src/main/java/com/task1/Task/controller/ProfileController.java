package com.task1.Task.controller;

import com.task1.Task.dto.Profiledto;
import com.task1.Task.dto.Userdto;
import com.task1.Task.entity.Profile;
import com.task1.Task.service.JWTService;
import com.task1.Task.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/insert/{id}")
    public ResponseEntity<Profiledto> insertProfileDetails(@PathVariable("id") Long id, @RequestBody Profiledto profiledto){

        return new ResponseEntity<>(profileService.insertProfileDetails(id,profiledto), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Profiledto> getProfileById(@PathVariable("id")Long id){
        return new ResponseEntity<>(profileService.getProfileById(id),HttpStatus.OK);
    }


    @PostMapping("/follow/{id}")
    public ResponseEntity<Profiledto> followAnotherId(@PathVariable("id") Long id, HttpServletRequest request){
        System.out.println(request);
        String token = request.getHeader("Authorization").substring(7);
        System.out.println(token);//Extract Token from Header
        Long  loggedInUserId = jwtService.extractUserId(token);
        System.out.println(loggedInUserId);

        return new ResponseEntity<>(profileService.followAnotherId(id,loggedInUserId),HttpStatus.OK);
    }

    @PostMapping("/unfollow/{id}")
    public ResponseEntity<Profiledto> unfollowAnotherId(@PathVariable("id") Long id){
        return new ResponseEntity<>(profileService.unfollowAnotherId(id),HttpStatus.OK);
    }
}
