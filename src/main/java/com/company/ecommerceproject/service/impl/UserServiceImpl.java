package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.config.AppConfig;
import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.service.dto.UserFormDTO;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.repository.RoleRepository;
import com.company.ecommerceproject.repository.UserRepository;
import com.company.ecommerceproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    AppConfig appConfig;

    @Override
    public Page<UserEnt> listAll(int pageNum) {
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return userRepo.findAll(pageable);
    }

    @Override
    public UserEnt getUserById(Integer id) throws UserNotFoundException {
        Optional<UserEnt> users = userRepo.findById(id);
        if (users.isPresent()) {
            return users.get();
        }
        throw new UserNotFoundException("Could not find any User with Id: "+id);
    }

    @Override
    public UserEnt save(UserEnt userEnt) {
        return userRepo.save(userEnt);
    }

    @Override
    public UserEnt createRegisterUser(UserFormDTO userFormDTO) {
        String encyptedPassword = appConfig.passwordEncoder().encode(userFormDTO.getPassword());
        UserEnt userEnt = new UserEnt();
        userEnt.setEmail(userFormDTO.getEmail());
        userEnt.setFirstName(userFormDTO.getFirstName());
        userEnt.setLastName(userFormDTO.getLastName());
        userEnt.setEncryptedPassword(encyptedPassword);
        userEnt.setGender(userFormDTO.getGender());
        return userEnt;
    }

    @Override
    public void setDefaultPermission(UserEnt userEnt) {
        userEnt.addRole(roleRepo.findByName("Customer"));
    }

    @Override
    public void softDelete(Integer id) throws UserNotFoundException {
        Optional<UserEnt> users = userRepo.findById(id);
        if (!users.isPresent()) {
            throw new UserNotFoundException("Could not find any User with ID "+id);
        }
        userRepo.delete(id);
    }
}
