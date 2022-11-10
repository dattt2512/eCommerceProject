package com.company.ecommerceproject.repository;

import com.company.ecommerceproject.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "select p from Product p where p.code = ?1")
    Product findProduct(String code);
}
