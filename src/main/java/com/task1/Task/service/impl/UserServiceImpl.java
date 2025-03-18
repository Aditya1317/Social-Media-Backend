package com.task1.Task.service.impl;

import com.task1.Task.config.ModelMapperConfig;
import com.task1.Task.dto.AuthResponse;
import com.task1.Task.dto.Userdto;
import com.task1.Task.entity.Profile;
import com.task1.Task.entity.User;
//import com.task1.Task.mapper.UserMapper;
import com.task1.Task.exception.ResourceNotFoundException;
import com.task1.Task.repository.ProfileRepository;
import com.task1.Task.repository.UserRepository;
import com.task1.Task.service.JWTService;
import com.task1.Task.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTServiceImpl jwtService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Userdto createUser(Userdto userdto) {

        User user= modelMapper.map(userdto,User.class);
        user.setPassword(encoder.encode(user.getPassword()));

        User saveduser=userRepository.save(user);

        Profile profile=new Profile();
        profile.setUser(saveduser);
        profileRepository.save(profile);

        return modelMapper.map(saveduser,Userdto.class);

    }


    @Override
    public AuthResponse verify(Userdto userdto) {
        User user = modelMapper.map(userdto, User.class);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            // Get full user details to fetch the ID
            User authenticatedUser = userRepository.findByName(user.getName());
//            User authenticatedUser = userRepository.findByName(user.getName())
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtService.generateToken(user.getName(), user.getId());
            Long userId = authenticatedUser.getId();

            return new AuthResponse(token, userId);
        }

        throw new RuntimeException("Authentication failed");
    }





//    @Override
//    public String verify(Userdto userdto) {
//        User user= modelMapper.map(userdto,User.class);
//
//        Authentication authentication=
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword()));
//
//        if(authentication.isAuthenticated()){
//            return jwtService.generateToken(user.getName());
//        }
//
//
//        return "fail";
//    }

    @Override
    public Userdto getUserById(Long id) {
        User user=userRepository.findById(id).orElseThrow( ()->new ResourceNotFoundException("No user found with Id:"+id));
        return modelMapper.map(user,Userdto.class);
    }


//    for angular
@Override
public Userdto updateUserById(Long id, Userdto userdto) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No User found for the Id:" + id));

    // Map updated fields
    user.setName(userdto.getName());
    user.setEmail(userdto.getEmail());
    user.setContactNo(userdto.getContactNo());
    user.setPassword(encoder.encode(user.getPassword())); // Encode password
    user.setDate(user.getDate());

    User updatedUser = userRepository.save(user);
    return modelMapper.map(updatedUser, Userdto.class);
}






//    old
//    @Override
//    public Userdto updateUserById(Long id, Userdto userdto) {
//
//        User user=userRepository.findById(id).orElseThrow( ()->new RuntimeException("No User found for the Id:"+id) );
//        User updatedUser=modelMapper.map(userdto,User.class);
//        updatedUser.setPassword(encoder.encode(user.getPassword()));
//        updatedUser.setId(id);
//        updatedUser.setDate(user.getDate());
//
//        userRepository.save(updatedUser);
//        return modelMapper.map(updatedUser,Userdto.class);
//
//    }

    @Override
    public Page<User> getAllUsers(int pageNumber, int pageSize, String sortBy, boolean sortOrder) {
        Sort sort = sortOrder ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);

        return userRepository.findAll(pageable);
    }




}
