package com.company.ecommerceproject.beans;

import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.entities.UserEnt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class MyUserDetails implements UserDetails {
    private UserEnt userEnt;

    public MyUserDetails(UserEnt userEnt) {
        this.userEnt = userEnt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = userEnt.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for(Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEnt.getPassword();
    }

    @Override
    public String getUsername() {
        return userEnt.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEnt.getEnabled();
    }

    public UserEnt getUser() {
        return userEnt;
    }
}
