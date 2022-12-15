package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.config.AppConfig;
import com.company.ecommerceproject.dto.response.UserDTO;
import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.mapper.BaseMapper;
import com.company.ecommerceproject.repository.RoleRepository;
import com.company.ecommerceproject.repository.UserRepository;
import com.company.ecommerceproject.service.UserService;
import com.company.ecommerceproject.ultis.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final BaseMapper<UserEnt, UserDTO> mapper = new BaseMapper<>(UserEnt.class, UserDTO.class);
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    AppConfig appConfig;

    @Override
    public List<UserDTO> listAll() {
        List<UserEnt> listEntity = userRepo.findAll();
        List<UserDTO> listData = mapper.toDtoBean(listEntity);
        return listData;
    }

    @Override
    public UserDTO getUserById(Integer id) throws UserNotFoundException {
        Optional<UserEnt> users = userRepo.findById(id);
        if (users.isPresent()) {
            UserDTO dto = mapper.toDtoBean(users.get());
            return dto;
        }
        throw new UserNotFoundException("Could not find any User with Id: "+id);
    }

    @Override
    public UserDTO save(UserDTO userDTO) {

        UserEnt entity;

        if(!DataUtils.isNullOrEmpty(userDTO.getUserId())) {
            entity = mapper.toPersistenceBean(userDTO);
//            entity.setCreatedDate(userDTO.getCreatedDate());
            updatePasswordCheck(entity, userDTO);
        } else {
            entity = mapper.toPersistenceBean(userDTO);
            entity.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepo.findByName("CUSTOMER"));
            entity.setRoles(roles);

            String encryptedPassword = appConfig.passwordEncoder().encode(userDTO.getPassword());

            entity.setPassword(encryptedPassword);

        }
        return mapper.toDtoBean(userRepo.save(entity));
    }

    private UserEnt updatePasswordCheck(UserEnt entity, UserDTO userDTO) {
        UserEnt userToCheckPass = userRepo.findById(userDTO.getUserId()).get();

        if (userDTO.getPassword().equals(userToCheckPass.getPassword())) {
            entity.setPassword(userDTO.getPassword());
        } else {
            entity.setPassword(appConfig.passwordEncoder().encode(userDTO.getPassword()));
        }

        return entity;
    }

    @Override
    public void delete(Integer id) throws UserNotFoundException {
        Optional<UserEnt> users = userRepo.findById(id);
        if (!users.isPresent()) {
            throw new UserNotFoundException("Could not find any User with ID "+id);
        }
        userRepo.delete(id);
    }
}
