package com.company.ecommerceproject.repository;

import com.company.ecommerceproject.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "select r from Role r where r.name = ?1")
    Role findByName(String name);
}
