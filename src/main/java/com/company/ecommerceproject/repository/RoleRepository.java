package com.company.ecommerceproject.repository;

import com.company.ecommerceproject.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Integer> {
//    @Query(value = "select r from Role r where r.name = ?1")
    Role findByName(String name);

    @Query(value = "select r.* from roles r \n" +
            "inner join users_roles ur on ur.role_id  = r.id \n" +
            "inner join users u on u.user_id = ur.user_id \n" +
            "where u.email = ?1", nativeQuery = true)
    List<Role> findRolesByEmail(String email);
}
