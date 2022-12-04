package com.company.ecommerceproject.repository.impl;

import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.repository.UserRepositoryCustom;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.UUID;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @Transactional
    public void delete(int id) {
        UserEnt userEntToDelete = getEntityManager().find(UserEnt.class, id);

        if (userEntToDelete.getUuid() == null || userEntToDelete.getUuid().isEmpty()) {
            userEntToDelete.setUuid(UUID.randomUUID().toString());
        }
        Date deletedDate = new Date();
        userEntToDelete.setDeletedDate(deletedDate);
        userEntToDelete = getEntityManager().merge(userEntToDelete);
    }
}
