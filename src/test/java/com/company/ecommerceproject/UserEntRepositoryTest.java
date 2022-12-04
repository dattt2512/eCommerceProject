package com.company.ecommerceproject;

import com.company.ecommerceproject.entities.Gender;
import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.repository.RoleRepository;
import com.company.ecommerceproject.repository.UserRepository;
import com.company.ecommerceproject.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserEntRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    UserServiceImpl userService;

    @Test
    public void testCreate() {
        UserEnt userEnt = new UserEnt();
        userEnt.setEmail("ttdat.2512@gmail.com");
        userEnt.setFirstName("Dat");
        userEnt.setLastName("Tran");
        userEnt.setGender(Gender.MALE);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = bCryptPasswordEncoder.encode("123456");

        userEnt.setEncryptedPassword(encryptPassword);
        userEnt.setEnabled(true);

        userRepo.save(userEnt);

        assertThat(userEnt).isNotNull();
        assertThat(userEnt.getUserId()).isGreaterThan(0);
    }

    @Test
    public void testRetrieve() {
        Integer userId = 1;
        Optional<UserEnt> result = userRepo.findById(userId);
        UserEnt userEnt = result.get();

        assertThat(result).isPresent();
        System.out.println(userEnt);
    }

    @Test
    public void testUpdate() {
        Integer userId = 1;
        Optional<UserEnt> result = userRepo.findById(userId);
        UserEnt userEnt = result.get();
        userEnt.setFirstName("Dang");
        userRepo.save(userEnt);

        UserEnt updatedUserEnt = userRepo.findById(userId).get();

        assertThat(updatedUserEnt.getFirstName()).isEqualTo("Dang");
    }

    @Test
    public void testDelete() {
        Integer userId = 1;
        userRepo.deleteById(userId);
        Optional<UserEnt> result = userRepo.findById(userId);

        assertThat(result).isNotPresent();
    }

    @Test
    public void testAddRoleToNewUser() {
        Role roleAdmin = roleRepo.findByName("Admin");

        UserEnt userEnt = new UserEnt();
        userEnt.setEmail("ngocly2308@gmail.com");
        userEnt.setFirstName("Ngoc");
        userEnt.setLastName("Ly");
        userEnt.setGender(Gender.FEMALE);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = bCryptPasswordEncoder.encode("686868");

        userEnt.setEncryptedPassword(encryptPassword);
        userEnt.setEnabled(true);
        userEnt.addRole(roleAdmin);

        UserEnt savedUserEnt = userRepo.save(userEnt);

        assertThat(savedUserEnt.getRoles().size()).isEqualTo(1);
    }

    @Test
    public void testAddRoleToExistingUser() {
        UserEnt userEnt = userRepo.findById(1).get();
        Role roleUser = roleRepo.findByName("User");
        Role roleAdmin = new Role(1);

        userEnt.addRole(roleUser);
        userEnt.addRole(roleAdmin);

        UserEnt savedUserEnt = userRepo.save(userEnt);

        assertThat(savedUserEnt.getRoles().size()).isEqualTo(2);
    }
}
