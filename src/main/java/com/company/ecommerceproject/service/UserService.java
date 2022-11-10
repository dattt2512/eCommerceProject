package com.company.ecommerceproject.service;

import com.company.ecommerceproject.dto.UserFormDTO;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.entities.User;
import org.springframework.data.domain.Page;

public interface UserService {
    public Page<User> listAll(int pageNum);

    User getUserById(Integer id) throws UserNotFoundException;

    User save(User user);

    User createRegisterUser(UserFormDTO userFormDTO);

    void setDefaultPermission(User newUser);

    void softDelete(Integer id) throws UserNotFoundException;
}
