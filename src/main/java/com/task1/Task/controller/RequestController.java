package com.task1.Task.controller;
import com.task1.Task.dto.Profiledto;
import com.task1.Task.service.JWTService;
import com.task1.Task.service.RequestService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;
    private final JWTService jwtService;

    public RequestController(RequestService requestService, JWTService jwtService) {
        this.requestService = requestService;
        this.jwtService = jwtService;
    }

    @PostMapping("/follow/{id}")
    public String followAnotherId(@PathVariable("id") Long id, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        Long loggedInUserId = jwtService.extractUserId(token);
        return requestService.followAnotherId(id, loggedInUserId);
    }

    @PostMapping("/accept-follow/{requestId}/{flowableProcessId}")
    public String acceptFollowRequest(@PathVariable("requestId") Long requestId, HttpServletRequest request,@PathVariable String flowableProcessId) {
        String token = request.getHeader("Authorization").substring(7);
        Long loggedInUserId = jwtService.extractUserId(token);
        return requestService.acceptFollowRequest(requestId, loggedInUserId,flowableProcessId);
    }

    @PostMapping("/reject-follow/{requestId}/{flowableProcessId}")
    public String rejectFollowRequest(@PathVariable("requestId") Long requestId, HttpServletRequest request,@PathVariable String flowableProcessId) {
        String token = request.getHeader("Authorization").substring(7);
        Long loggedInUserId = jwtService.extractUserId(token);
        return requestService.rejectFollowRequest(requestId, loggedInUserId,flowableProcessId);
    }

//    @GetMapping("/notifications")
//    public ResponseEntity<List<FollowerRequest>> getPendingFollowerRequests(HttpServletRequest request) {
//        String token = request.getHeader("Authorization").substring(7);
//        Long loggedInUserId = jwtService.extractUserId(token);
//        List<FollowerRequest> pendingRequests = requestService.getPendingFollowerRequests(loggedInUserId);
//        return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
//    }

    @PostMapping("/unfollow/{requestId}")
    public ResponseEntity<Profiledto> unfollowAnotherId(@PathVariable("requestId") Long requestId, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        Long loggedInUserId = jwtService.extractUserId(token);
        return new ResponseEntity<>(requestService.unfollowAnotherId(requestId, loggedInUserId), HttpStatus.OK);
    }
}
