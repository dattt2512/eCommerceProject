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

<<<<<<< HEAD
    @GetMapping("/admin/users/page/{pageNum}")
    public List<UserEnt> adminPage(@PathVariable("pageNum") int pageNum, Principal principal) {
        String username = principal.getName();
        System.out.println("Username: " + username);
        Page<UserEnt> page = userService.listAll(pageNum);
        List<UserEnt> usersList = page.getContent();

//        model.addAttribute("currentPage", pageNum);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalUsers", page.getTotalElements());

//        List<User> usersList = userService.listAll();
//        model.addAttribute("usersList", usersList);
        return usersList;
    }

    @GetMapping("/admin")
    public List<UserEnt> listAll() {
        return userService.listAll(1).getContent();
=======
    @GetMapping()
    public List<UserDTO> listAll() {
        return userService.listAll();
>>>>>>> 8d537cc (Change to Restful API)
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable(value = "id") Integer id) {
        try {
<<<<<<< HEAD
            UserEnt user = userService.getUserById(id);
            List<Role> roleList = roleService.listAll();
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", userEnt);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            return "user_form";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/users/page/1";
        }
    }

    @PostMapping("/admin/users/save")
    public String saveUser(UserEnt user, RedirectAttributes ra) {
        try {
            UserEnt getUser = userService.getUserById(user.getUserId());
            if (getUser.getEncryptedPassword().equals(user.getEncryptedPassword())) {
                userService.save(user);
                System.out.println("equals");
            } else {
                String rawPass = userEnt.getEncryptedPassword();
                String encryptedPass = appConfig.passwordEncoder().encode(rawPass);
                userEnt.setEncryptedPassword(encryptedPass);
                userService.save(userEnt);
            }
=======
            return userService.getUserById(id);
>>>>>>> 8d537cc (Change to Restful API)
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
