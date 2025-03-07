//package com.task1.Task.mapper;
//
//import com.task1.Task.dto.Profiledto;
//import com.task1.Task.entity.Profile;
//
//public class ProfileMapper {
//
//    public static Profiledto mapToProfileDto(Profile profile){
//        return new Profiledto(profile.getId(),
//                profile.getFollowers(),
//                profile.getFollowing(),
//                profile.getNoOfPosts(),
//                profile.getBio(),
//                );
//    }
//
//    public static Profile mapToProfile(Profiledto profiledto){
//        return new Profile(profiledto.getId(),
//                profiledto.getFollowers(),
//                profiledto.getFollowing(),
//                profiledto.getNoOfPosts(),
//                profiledto.getBio(),
//                profiledto.getUserId());
//    }
//}
