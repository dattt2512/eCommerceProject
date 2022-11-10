package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.repository.RoleRepository;
import com.company.ecommerceproject.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepo;


    @Override
    public List<Role> listAll() {
        return roleRepo.findAll();
    }
}
