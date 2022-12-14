package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.repository.RoleRepository;
import com.company.ecommerceproject.repository.UserRepository;
import com.company.ecommerceproject.ultis.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEnt userEnt = userRepo.findByEmail(email);

        if (userEnt == null || userEnt.getDeletedDate() != null) {
            throw new UsernameNotFoundException("Could not find any User with email: "+email);
        } else {
            List<Role> roles = roleRepo.findRolesByEmail(email);
            List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Role role: roles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(jwtTokenUtil.getRolePrefix() + role.getName()));
            }
            return new User(userEnt.getEmail(), userEnt.getPassword(), grantedAuthorities);
        }
    }
}
