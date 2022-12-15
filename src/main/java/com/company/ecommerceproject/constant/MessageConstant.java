package com.company.ecommerceproject.constant;

import org.springframework.stereotype.Component;

@Component
public class MessageConstant {
    String INVALID_TOKEN = "Invalid Token";
    String VALID_TOKEN = "Valid token for user ";
    String USERNAME_OR_PASSWORD_EMPTY = "Username or Password should not be empty";
    String USERNAME_OR_PASSWORD_INVALID = "Bad credentials";
    String USERNAME_INACTIVE = "User is inactive";
    String USER_NOT_PERMISSIONS = "Your account doesn't have permission";
    String SYSTEM_ERROR = "System error";
}
