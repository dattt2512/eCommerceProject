package com.company.ecommerceproject.service;

import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.service.dto.UserFormDTO;
import com.company.ecommerceproject.exception.UserNotFoundException;
import org.springframework.data.domain.Page;

public interface UserService {
    public Page<UserEnt> listAll(int pageNum);

    UserEnt getUserById(Integer id) throws UserNotFoundException;

    UserEnt save(UserEnt userEnt);

    UserEnt createRegisterUser(UserFormDTO userFormDTO);

    void setDefaultPermission(UserEnt newUserEnt);

    void softDelete(Integer id) throws UserNotFoundException;
}
