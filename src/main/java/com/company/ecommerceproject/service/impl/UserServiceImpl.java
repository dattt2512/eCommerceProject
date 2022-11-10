package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.config.AppConfig;
import com.company.ecommerceproject.dto.UserForm;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.model.User;
import com.company.ecommerceproject.repository.RoleRepository;
import com.company.ecommerceproject.repository.UserRepository;
import com.company.ecommerceproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Page<User> listAll(int pageNum) {
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return userRepo.findAll(pageable);
    }

    @Override
    public User getUserById(Integer id) throws UserNotFoundException {
        Optional<User> users = userRepo.findById(id);
        if (users.isPresent()) {
            return users.get();
        }
        throw new UserNotFoundException("Could not find any User with Id: "+id);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public User createRegisterUser(UserForm userForm) {
        String encyptedPassword = appConfig.passwordEncoder().encode(userForm.getPassword());
        User user = new User();
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEncryptedPassword(encyptedPassword);
        user.setGender(userForm.getGender());
        return user;
    }

    @Override
    public void setDefaultPermission(User user) {
        user.addRole(roleRepo.findByName("Customer"));
    }

    @Override
    public void softDelete(Integer id) throws UserNotFoundException {
        Optional<User> users = userRepo.findById(id);
        if (!users.isPresent()) {
            throw new UserNotFoundException("Could not find any User with ID "+id);
        }
        userRepo.softDelete(id);
    }
}
