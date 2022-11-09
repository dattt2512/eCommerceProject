package com.company.ecommerceproject.service;

import com.company.ecommerceproject.dto.UserForm;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    public Page<User> listAll(int pageNum);

    User getUserById(Integer id) throws UserNotFoundException;

    User save(User user);

    User createRegisterUser(UserForm userForm);

    void setDefaultPermission(User newUser);

    void softDelete(Integer id) throws UserNotFoundException;
}
