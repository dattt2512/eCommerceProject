package com.company.ecommerceproject.repository;

import com.company.ecommerceproject.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {

    @Query(value = "select p from Product p where p.code = ?1")
    Product findProduct(String code);

    Product findByCode(String code);

    @Query(value = "select p from Product p where p.deletedDate is null ")
    List<Product> findAll();
}
