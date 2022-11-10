package com.company.ecommerceproject;

import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepo;

    @Test
    public void createProductTest() {
        Product product = new Product();
        product.setCode("0003");
        product.setName("Srping Boot");
        product.setPrice(61.9);
        product.setCreateDate(new Date());
        product.setQuantity(1000);

        Product savedProduct = productRepo.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void retrieveTest() {
        Integer id = 2;
        Optional<Product> products = productRepo.findById(id);

        assertThat(products).isPresent();
        System.out.println(products.get());
    }

    @Test
    public void updateTest() {
        Integer id = 1;
        Optional<Product> products = productRepo.findById(id);
        Product product = products.get();
        product.setQuantity(900);

        productRepo.save(product);
        Product updatedProduct = productRepo.findById(id).get();

        assertThat(updatedProduct.getQuantity()).isEqualTo(900);
    }

    @Test
    public void deleteTest() {
        Integer id = 3;
        productRepo.deleteById(id);

        Optional<Product> products = productRepo.findById(id);

        assertThat(products).isNotPresent();
    }
}
