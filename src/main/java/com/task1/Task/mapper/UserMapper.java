//package com.task1.Task.mapper;
//
//import com.task1.Task.dto.Userdto;
//import com.task1.Task.entity.User;
//
//public class UserMapper {
//
//    public static Userdto mapToUserdto(User user) {
//        return new Userdto(
//                user.getId(),
//                user.getName(),
//                user.getEmail(),
//                user.getContactNo(),
//                user.getDate()
//        );
//    }
//
//    public static User mapToUser(Userdto userdto) {
//        return new User(
//                userdto.getId(),
//                userdto.getName(),
//                userdto.getEmail(),
//                userdto.getContactNo(),
//                userdto.getDate()
//
//        );
//    }
//}
