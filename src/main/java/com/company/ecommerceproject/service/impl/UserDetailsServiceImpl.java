package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.beans.MyUserDetails;
import com.company.ecommerceproject.entities.User;
import com.company.ecommerceproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if (user == null || user.isDeleted()) {
            throw new UsernameNotFoundException("Could not find any User with email: "+email);
        }

        return new MyUserDetails(user);
    }
}
