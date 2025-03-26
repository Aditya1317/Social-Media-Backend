package com.task1.Task.controller;

import com.task1.Task.dto.Profiledto;
import com.task1.Task.dto.Userdto;
import com.task1.Task.entity.FollowerRequest;
import com.task1.Task.entity.Profile;
import com.task1.Task.service.JWTService;
import com.task1.Task.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/accept-follow/{requestId}")
    public ResponseEntity<Profiledto> acceptFollowRequest(@PathVariable("requestId") Long requestId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Extract Token
        Long loggedInUserId = jwtService.extractUserId(token); // Get Logged-in User ID

        return new ResponseEntity<>(profileService.acceptFollowRequest(requestId, loggedInUserId), HttpStatus.OK);
    }

    @PostMapping("/reject-follow/{requestId}")
    public ResponseEntity<Profiledto> rejectFollowRequest(@PathVariable("requestId") Long requestId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Extract Token
        Long loggedInUserId = jwtService.extractUserId(token); // Get Logged-in User ID

        return new ResponseEntity<>(profileService.rejectFollowRequest(requestId, loggedInUserId), HttpStatus.OK);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<FollowerRequest>> getPendingFollowerRequests(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Extract token
        Long loggedInUserId = jwtService.extractUserId(token); // Get logged-in user ID

        List<FollowerRequest> pendingRequests = profileService.getPendingFollowerRequests(loggedInUserId);

        return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
    }

    @PostMapping("/unfollow/{requestId}")
    public ResponseEntity<Profiledto> unfollowAnotherId(@PathVariable("requestId") Long requestId, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7); // Extract Token
        Long loggedInUserId = jwtService.extractUserId(token); // Get Logged-in User ID

        return new ResponseEntity<>(profileService.unfollowAnotherId(requestId, loggedInUserId), HttpStatus.OK);
    }
}
