package com.company.ecommerceproject.validator;

import com.company.ecommerceproject.dto.response.UserDTO;
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
        return clazz == UserDTO.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        // Kiểm tra các field của userForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.UserForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.UserForm.firstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.UserForm.lastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.UserForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "NotEmpty.UserForm.gender");

        if (!this.emailValidator.isValid(userDTO.getEmail())) {
            errors.rejectValue("email", "Pattern.UserForm.email");
        }

        if (!(2 < userDTO.getEmail().length() && userDTO.getEmail().length() < 45)) {
            errors.rejectValue("email", "Size.UserForm.email");
        }

        if (!(2 <= userDTO.getFirstName().length() && userDTO.getFirstName().length() <= 45)) {
            errors.rejectValue("firstName", "Size.UserForm.firstName");
        }

        if (!(2 <= userDTO.getLastName().length() && userDTO.getLastName().length() <= 45)) {
            errors.rejectValue("lastName", "Size.UserForm.lastName");
        }

        if (!(8 <= userDTO.getPassword().length() && userDTO.getPassword().length() <= 15)) {
            errors.rejectValue("password", "Size.UserForm.password");
        }

        UserEnt dbUserEnt = userRepo.findByEmail(userDTO.getEmail());
        if (dbUserEnt != null) {
        // Email đã được sử dụng bởi tài khoản khác.
            errors.rejectValue("email", "Duplicate.UserForm.email");
        }
    }
}
