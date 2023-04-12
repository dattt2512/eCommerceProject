package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.config.AppConfig;
import com.company.ecommerceproject.dto.response.UserDTO;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.service.impl.RoleServiceImpl;
import com.company.ecommerceproject.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/admin/users")
public class AdminController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    AppConfig appConfig;

    @Autowired
    RoleServiceImpl roleService;

    @GetMapping()
    public List<UserDTO> listAll() {
        return userService.listAll();
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable(value = "id") Integer id) {
        try {
            return userService.getUserById(id);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
  
    @PutMapping("/{id}")
    public UserDTO updateUser(@RequestBody UserDTO user) {
        return userService.save(user);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            userService.delete(id);
            return new ResponseEntity("User deleted successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity("User " + id + " not found", HttpStatus.FORBIDDEN);
        }
    }
}
