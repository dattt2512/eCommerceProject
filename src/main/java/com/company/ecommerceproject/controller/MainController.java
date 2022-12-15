package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.dto.response.UserDTO;
import com.company.ecommerceproject.validator.UserFormValidator;
import com.company.ecommerceproject.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api")
public class MainController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserFormValidator userFormValidator;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target: " + target);

        if (target.getClass() == UserDTO.class) {
            dataBinder.setValidator(userFormValidator);
        }
    }

    @PostMapping("/register")
    public UserDTO saveRegisterUser(@RequestBody @Validated UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @GetMapping("/logoutSuccessful")
    public ResponseEntity<?> logoutSuccessful() {
        return new ResponseEntity("Logout successfully", HttpStatus.OK);
    }
}
