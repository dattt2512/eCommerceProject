package com.company.ecommerceproject;

import com.company.ecommerceproject.model.Gender;
import com.company.ecommerceproject.model.Role;
import com.company.ecommerceproject.model.User;
import com.company.ecommerceproject.repository.RoleRepository;
import com.company.ecommerceproject.repository.UserRepository;
import com.company.ecommerceproject.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    UserServiceImpl userService;

    @Test
    public void testCreate() {
        User user = new User();
        user.setEmail("ttdat.2512@gmail.com");
        user.setFirstName("Dat");
        user.setLastName("Tran");
        user.setGender(Gender.MALE);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = bCryptPasswordEncoder.encode("123456");

        user.setEncryptedPassword(encryptPassword);
        user.setEnabled(true);

        userRepo.save(user);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isGreaterThan(0);
    }

    @Test
    public void testRetrieve() {
        Integer userId = 1;
        Optional<User> result = userRepo.findById(userId);
        User user = result.get();

        assertThat(result).isPresent();
        System.out.println(user);
    }

    @Test
    public void testUpdate() {
        Integer userId = 1;
        Optional<User> result = userRepo.findById(userId);
        User user = result.get();
        user.setFirstName("Dang");
        userRepo.save(user);

        User updatedUser = userRepo.findById(userId).get();

        assertThat(updatedUser.getFirstName()).isEqualTo("Dang");
    }

    @Test
    public void testDelete() {
        Integer userId = 1;
        userRepo.deleteById(userId);
        Optional<User> result = userRepo.findById(userId);

        assertThat(result).isNotPresent();
    }

    @Test
    public void testAddRoleToNewUser() {
        Role roleAdmin = roleRepo.findByName("Admin");

        User user = new User();
        user.setEmail("ngocly2308@gmail.com");
        user.setFirstName("Ngoc");
        user.setLastName("Ly");
        user.setGender(Gender.FEMALE);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = bCryptPasswordEncoder.encode("686868");

        user.setEncryptedPassword(encryptPassword);
        user.setEnabled(true);
        user.addRole(roleAdmin);

        User savedUser = userRepo.save(user);

        assertThat(savedUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    public void testAddRoleToExistingUser() {
        User user = userRepo.findById(1).get();
        Role roleUser = roleRepo.findByName("User");
        Role roleAdmin = new Role(1);

        user.addRole(roleUser);
        user.addRole(roleAdmin);

        User savedUser = userRepo.save(user);

        assertThat(savedUser.getRoles().size()).isEqualTo(2);
    }
}
