package com.company.ecommerceproject.dto.response;

import com.company.ecommerceproject.dto.BaseDto;
import com.company.ecommerceproject.entities.Gender;
import com.company.ecommerceproject.entities.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseDto<String> implements Serializable {

    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private Gender gender;

    private boolean enabled;

    private Set<Role> roles;
}
