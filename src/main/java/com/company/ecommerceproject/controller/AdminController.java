package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.config.AppConfig;
import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.service.impl.RoleServiceImpl;
import com.company.ecommerceproject.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    AppConfig appConfig;

    @Autowired
    RoleServiceImpl roleService;

    @GetMapping("/admin/users/page/{pageNum}")
    public String adminPage(Model model, @PathVariable("pageNum") int pageNum, Principal principal) {
        String username = principal.getName();
        System.out.println("Username: " + username);
        Page<UserEnt> page = userService.listAll(pageNum);
        List<UserEnt> usersList = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalUsers", page.getTotalElements());

//        List<User> usersList = userService.listAll();
        model.addAttribute("usersList", usersList);
        return "users";
    }

    @GetMapping("/admin/users/new")
    public String showNewForm(Model model) {
//        UserEnt user = new UserEnt();
//        List<Role> roleList = roleService.listAll();
//        model.addAttribute("roleList", roleList);
//        model.addAttribute("user", user);
//        model.addAttribute("pageTitle", "Add New User");
        return "user_form";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String showEditForm(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
        try {
            UserEnt user = userService.getUserById(id);
            List<Role> roleList = roleService.listAll();
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
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
                String rawPass = user.getEncryptedPassword();
                String encryptedPass = appConfig.passwordEncoder().encode(rawPass);
                user.setEncryptedPassword(encryptedPass);
                userService.save(user);
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        ra.addFlashAttribute("message", "User has been saved succesfully.");
        return "redirect:/admin/users/page/1";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            userService.softDelete(id);
            ra.addFlashAttribute("message", "User ID "+id+ " has been deleted.");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/users/page/1";
    }
}
