package com.company.ecommerceproject.service;

import com.company.ecommerceproject.dto.response.UserDTO;
import com.company.ecommerceproject.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    public List<UserDTO> listAll();

    UserDTO getUserById(Integer id) throws UserNotFoundException;

    UserDTO save(UserDTO userDTO);

    void delete(Integer id) throws UserNotFoundException;
}
