package com.company.ecommerceproject.service;

import com.company.ecommerceproject.service.dto.ProductFormDTO;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<Product> listAll(int pageNum);

    Product save(ProductFormDTO productFormDTO);

    Product getProductById(Integer id) throws ProductNotFoundException;

    void delete(Integer id) throws ProductNotFoundException;

    Product findProduct(String code);
}
