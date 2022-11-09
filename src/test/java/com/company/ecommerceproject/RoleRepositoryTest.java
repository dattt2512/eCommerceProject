package com.company.ecommerceproject;

import com.company.ecommerceproject.model.Role;
import com.company.ecommerceproject.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {
    @Autowired private RoleRepository roleRepo;

    @Test
    public void testCreateRoles() {
        Role admin = new Role("Admin");
        Role editor = new Role("Editor");
        Role user = new Role("User");

        roleRepo.saveAll(List.of(admin, editor, user));

        List<Role> roleList = roleRepo.findAll();

        assertThat(roleList.size()).isEqualTo(3);
    }
}
