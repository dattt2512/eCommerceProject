package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.dto.response.ProductDTO;
import com.company.ecommerceproject.dto.response.ProductFormDTO;
import com.company.ecommerceproject.dto.response.UserDTO;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.entities.UserEnt;
import com.company.ecommerceproject.exception.ProductNotFoundException;
import com.company.ecommerceproject.mapper.BaseMapper;
import com.company.ecommerceproject.repository.ProductRepository;
import com.company.ecommerceproject.service.ProductService;
import com.company.ecommerceproject.ultis.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final BaseMapper<Product, ProductDTO> mapper = new BaseMapper<>(Product.class, ProductDTO.class);
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepo;

    @Override
    public List<ProductDTO> listAll() {
        return mapper.toDtoBean(productRepo.findAll());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ProductDTO save(ProductDTO productDTO) {

        Product product;
        product = mapper.toPersistenceBean(productDTO);
        if (productDTO.getFileData() != null) {
            byte[] image = null;
            try {
                image = productDTO.getFileData().getBytes();
            } catch (IOException e) {
            }
            if (image != null && image.length > 0) {
                product.setImage(image);
            }
        }
        return mapper.toDtoBean(productRepo.save(product));
    }

    @Override
    public ProductDTO getProductById(Integer id) throws ProductNotFoundException {
        Optional<Product> products = productRepo.findById(id);
        if (products.isPresent()) {
            return mapper.toDtoBean(products.get());
        }
        throw new ProductNotFoundException("Could not find any Product with Id: "+id);
    }

    @Override
    public void delete(Integer id) throws ProductNotFoundException {
        Optional<Product> products = productRepo.findById(id);
        if (!products.isPresent()) {
            throw new ProductNotFoundException("Could not find any Product with ID "+id);
        }
        productRepo.delete(id);
    }

    @Override
    public Product findProductByCode(String code) {
        Product product = productRepo.findByCode(code);
        return product;
    }
}
