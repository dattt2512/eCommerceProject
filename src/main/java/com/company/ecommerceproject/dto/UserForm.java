package com.company.ecommerceproject.dto;

import com.company.ecommerceproject.model.Gender;
import com.company.ecommerceproject.model.Role;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public class UserForm {
    private String email;
    private String firstName;
    private String lastName;
    @Size(min = 8, max = 15, message = "Password must be between 8-15 characters")
    private String password;
    private String confirmPassword;
    private Gender gender;
    private boolean enabled;
    private Set<Role> roles;

    public UserForm() {

    }

    public UserForm(String email,
                    String firstName, String lastName,
                    String password, String confirmPassword,
                    Gender gender, Set<Role> roles) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.gender = gender;
        this.enabled = false;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", gender=" + gender +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }
}
