package com.company.ecommerceproject.service;

import com.company.ecommerceproject.dto.response.ProductDTO;
import com.company.ecommerceproject.dto.response.ProductFormDTO;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<ProductDTO> listAll();

    ProductDTO save(ProductDTO productDTO);

    ProductDTO getProductById(Integer id) throws ProductNotFoundException;

    void delete(Integer id) throws ProductNotFoundException;

    Product findProductByCode(String code);
}
