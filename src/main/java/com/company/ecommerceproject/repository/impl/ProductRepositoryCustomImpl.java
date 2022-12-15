package com.company.ecommerceproject.repository.impl;

import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.repository.ProductRepository;
import com.company.ecommerceproject.repository.ProductRepositoryCustom;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Product productToDelete = getEntityManager().find(Product.class, id);

        productToDelete.setDeletedDate(LocalDateTime.now());
        productToDelete = getEntityManager().merge(productToDelete);
    }
}
