package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.dto.ProductFormDTO;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.entities.User;
import com.company.ecommerceproject.exception.ProductNotFoundException;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.repository.ProductRepository;
import com.company.ecommerceproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepo;

    @Override
    public Page<Product> listAll(int pageNum) {
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return productRepo.findAll(pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Product save(ProductFormDTO productFormDTO) {
        String code = productFormDTO.getCode();
        Product product = null;
        boolean isNew = false;

        if (code != null) {
            product = productRepo.findProduct(code);
        }
        if (product == null) {
            isNew = true;
            product = new Product();
            product.setCreateDate(new Date());
        }
        product.setCode(code);
        product.setName(productFormDTO.getName());
        product.setPrice(productFormDTO.getPrice());
        product.setQuantity(productFormDTO.getQuantity());

        if (productFormDTO.getFileData() != null) {
            byte[] image = null;
            try {
                image = productFormDTO.getFileData().getBytes();
            } catch (IOException e) {
            }
            if (image != null && image.length > 0) {
                product.setImage(image);
            }
        }
        Product product1 = productRepo.save(product);
        return product1;
    }

    @Override
    public Product getProductById(Integer id) throws ProductNotFoundException {
        Optional<Product> products = productRepo.findById(id);
        if (products.isPresent()) {
            return products.get();
        }
        throw new ProductNotFoundException("Could not find any Product with Id: "+id);
    }

    @Override
    public void delete(Integer id) throws ProductNotFoundException {
        Optional<Product> products = productRepo.findById(id);
        if (!products.isPresent()) {
            throw new ProductNotFoundException("Could not find any Product with ID "+id);
        }
        productRepo.deleteById(id);
    }
}
