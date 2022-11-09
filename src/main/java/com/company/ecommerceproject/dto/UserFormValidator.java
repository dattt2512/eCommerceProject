package com.company.ecommerceproject.dto;

import com.company.ecommerceproject.model.User;
import com.company.ecommerceproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserFormValidator implements Validator {
    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm userForm = (UserForm) target;

        // Kiểm tra các field của userForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.UserForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.UserForm.firstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.UserForm.lastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.UserForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.UserForm.confirmPassword");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "NotEmpty.UserForm.gender");

        if (!this.emailValidator.isValid(userForm.getEmail())) {
            errors.rejectValue("email", "Pattern.UserForm.email");
        }

        if (!(2 < userForm.getEmail().length() && userForm.getEmail().length() < 45)) {
            errors.rejectValue("email", "Size.UserForm.email");
        }

        if (!(2 <= userForm.getFirstName().length() && userForm.getFirstName().length() <= 45)) {
            errors.rejectValue("firstName", "Size.UserForm.firstName");
        }

        if (!(2 <= userForm.getLastName().length() && userForm.getLastName().length() <= 45)) {
            errors.rejectValue("lastName", "Size.UserForm.lastName");
        }

        if (!(8 <= userForm.getPassword().length() && userForm.getPassword().length() <= 15)) {
            errors.rejectValue("password", "Size.UserForm.password");
        }

        User dbUser = userRepo.findByEmail(userForm.getEmail());
        if (dbUser != null) {
        // Email đã được sử dụng bởi tài khoản khác.
            errors.rejectValue("email", "Duplicate.UserForm.email");
        }
        if (!errors.hasErrors()) {
            if (!userForm.getConfirmPassword().equals(userForm.getPassword())) {
                errors.rejectValue("confirmPassword", "Match.UserForm.confirmPassword");
            }
        }
    }
}
