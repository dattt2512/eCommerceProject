package com.company.ecommerceproject.repository;

import com.company.ecommerceproject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Override
    @Query("select u from User u where u.deleted = false")
    Page<User> findAll(Pageable pageable);

    @Query(value = "select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update users u set u.deleted = true where u.user_id = ?1", nativeQuery = true)
    void softDelete(Integer id);
}
