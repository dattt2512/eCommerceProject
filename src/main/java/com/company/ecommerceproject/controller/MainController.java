package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.beans.MyUserDetails;
import com.company.ecommerceproject.dto.UserFormDTO;
import com.company.ecommerceproject.validator.UserFormValidator;
import com.company.ecommerceproject.entities.User;
import com.company.ecommerceproject.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
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

        if (target.getClass() == UserFormDTO.class) {
            dataBinder.setValidator(userFormValidator);
        }
    }

    @GetMapping
    public String homePage(Model model, Principal principal) {
        if (principal != null) {
            MyUserDetails loginedUserDetails = (MyUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            User loginedUser = loginedUserDetails.getUser();
            model.addAttribute("loginedUser", loginedUser);
        }
        model.addAttribute("message", "WELCOME TO MY APPLICATION");
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/logoutSuccessful")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }

    @GetMapping("/403")
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            MyUserDetails loginedUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User loginedUser = loginedUserDetails.getUser();
            model.addAttribute("loginedUser", loginedUser);

            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
        }

        return "403Page";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        UserFormDTO userFormDTO = new UserFormDTO();
        model.addAttribute("userForm", userFormDTO);
        return "registerPage";
    }

    @PostMapping("/register")
    public String saveRegister(Model model,
                               @ModelAttribute("userForm") @Valid UserFormDTO userFormDTO,
                               BindingResult result, final RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "registerPage";
        }
        System.out.println(userFormDTO);
        User newUser = null;
        try {
            newUser = userService.createRegisterUser(userFormDTO);
            userService.setDefaultPermission(newUser);
            userService.save(newUser);
        }
        // Other error!!
        catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registerPage";
        }
        ra.addFlashAttribute("flashUser", newUser);
        return "redirect:/registerSuccessful";
    }

    @GetMapping("/registerSuccessful")
    public String showRegisterSuccesssfulPage() {
        return "registerSuccessful";
    }

}
