package com.company.ecommerceproject.security;

import com.company.ecommerceproject.ultis.JwtTokenUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class JwtTokenResponse {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private long expiresIn = jwtTokenUtil.getExpiration();
}
