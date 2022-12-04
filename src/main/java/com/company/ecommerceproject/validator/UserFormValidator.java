package com.company.ecommerceproject.validator;

import com.company.ecommerceproject.service.dto.UserFormDTO;
import com.company.ecommerceproject.entities.UserEnt;
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
        return clazz == UserFormDTO.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserFormDTO userFormDTO = (UserFormDTO) target;

        // Kiểm tra các field của userForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.UserForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.UserForm.firstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.UserForm.lastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.UserForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.UserForm.confirmPassword");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "NotEmpty.UserForm.gender");

        if (!this.emailValidator.isValid(userFormDTO.getEmail())) {
            errors.rejectValue("email", "Pattern.UserForm.email");
        }

        if (!(2 < userFormDTO.getEmail().length() && userFormDTO.getEmail().length() < 45)) {
            errors.rejectValue("email", "Size.UserForm.email");
        }

        if (!(2 <= userFormDTO.getFirstName().length() && userFormDTO.getFirstName().length() <= 45)) {
            errors.rejectValue("firstName", "Size.UserForm.firstName");
        }

        if (!(2 <= userFormDTO.getLastName().length() && userFormDTO.getLastName().length() <= 45)) {
            errors.rejectValue("lastName", "Size.UserForm.lastName");
        }

        if (!(8 <= userFormDTO.getPassword().length() && userFormDTO.getPassword().length() <= 15)) {
            errors.rejectValue("password", "Size.UserForm.password");
        }

        UserEnt dbUserEnt = userRepo.findByEmail(userFormDTO.getEmail());
        if (dbUserEnt != null) {
        // Email đã được sử dụng bởi tài khoản khác.
            errors.rejectValue("email", "Duplicate.UserForm.email");
        }
        if (!errors.hasErrors()) {
            if (!userFormDTO.getConfirmPassword().equals(userFormDTO.getPassword())) {
                errors.rejectValue("confirmPassword", "Match.UserForm.confirmPassword");
            }
        }
    }
}
