package com.company.ecommerceproject.repository;

import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.entities.UserEnt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEnt, Integer>, UserRepositoryCustom {

    @Override
    @Query("select u from UserEnt u where u.deletedDate is null ")
    Page<UserEnt> findAll(Pageable pageable);

    @Query(value = "select u from UserEnt u where u.email = ?1")
    UserEnt findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update users u set u.deleted_date = GETDATE() where u.user_id = ?1", nativeQuery = true)
    void softDelete(Integer id);

    @Query(value = "select u from UserEnt u where u.deletedDate is null ")
    List<UserEnt> findAll();
}
