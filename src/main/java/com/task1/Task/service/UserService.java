package com.task1.Task.service;

import com.task1.Task.dto.Userdto;
import com.task1.Task.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    Userdto createUser(Userdto userdto);

    Userdto getUserById(Long id);

    Userdto updateUserById(Long id, Userdto userdto);

    Page<User> getAllUsers(int pageNumber, int pageSize, String userId, boolean sortby);

    String verify(Userdto userdto);
}
