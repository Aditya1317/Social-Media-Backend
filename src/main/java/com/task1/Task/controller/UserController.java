package com.task1.Task.controller;


import com.task1.Task.dto.Userdto;
import com.task1.Task.entity.User;
import com.task1.Task.exception.ApiResponse;
import com.task1.Task.exception.ResourceNotFoundException;
import com.task1.Task.service.UserService;
import com.task1.Task.service.impl.UserServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;



    @PostMapping("/create")
    public ResponseEntity<Userdto> createUser(@RequestBody Userdto userdto){
        Userdto createdUser=userService.createUser(userdto);
        return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public String login(@RequestBody Userdto userdto){
        System.out.println(userdto);
        return userService.verify(userdto);
    }

    @GetMapping("/getdetails/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") Long id) throws ResourceNotFoundException {

//        return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
        return ResponseEntity.ok(new ApiResponse(userService.getUserById(id)));
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<Userdto> updateUserById(@PathVariable("id") Long id,@RequestBody Userdto userdto){
//
//        return new ResponseEntity<>(userService.updateUserById(id,userdto),HttpStatus.CREATED);
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Userdto>> updateUserById(
            @PathVariable("id") Long id,
            @RequestBody Userdto userdto) {

        Userdto updatedUser = userService.updateUserById(id, userdto);

        ApiResponse<Userdto> apiResponse = new ApiResponse<>(updatedUser);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }




//    @GetMapping("/getAllUsers")
//    public Page<User> getAllUsers(
//            @RequestParam(defaultValue = "1")int page,
//            @RequestParam(defaultValue = "5")int size,
//            @RequestParam(defaultValue = "id")String sortby,
//            @RequestParam(defaultValue = "true") boolean ascending){
//
//
//return userService.getAllUsers(page,size,sortby,ascending);
//    }

//    @GetMapping("/getAllUsers")
//    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "true") boolean ascending) {
//
//        // Call the service to get paginated and sorted data
//        Page<User> users = userService.getAllUsers(page, size, sortBy, ascending);
//
//        // Wrap the data in ApiResponse
//        ApiResponse<Page<User>> apiResponse = new ApiResponse<>(users);
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {


        Page<User> users = userService.getAllUsers(page, size, sortBy, ascending);


        return ResponseEntity.ok(new ApiResponse<>(users));
    }



}
