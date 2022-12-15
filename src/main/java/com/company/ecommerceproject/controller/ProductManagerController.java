package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.dto.response.ProductDTO;
import com.company.ecommerceproject.dto.response.ProductFormDTO;
import com.company.ecommerceproject.exception.ProductNotFoundException;
import com.company.ecommerceproject.service.OrderService;
import com.company.ecommerceproject.service.impl.ProductServiceImpl;
import com.company.ecommerceproject.validator.ProductFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/admin/products")
public class ProductManagerController {

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    ProductFormValidator productFormValidator;

    @Autowired
    OrderService orderService;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);
        if (target.getClass() == ProductDTO.class) {
            dataBinder.setValidator(productFormValidator);
        }
    }

    @GetMapping
    public List<ProductDTO> listAllProduct() {
        return productService.listAll();
    }

    @PostMapping
    public ProductDTO newProduct(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable(value = "id") Integer id) {
        try {
            return productService.getProductById(id);
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Delete Product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Integer id) {
        try {
            productService.delete(id);
            return new ResponseEntity("Product deleted successfully", HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity("Product " + id + " not found", HttpStatus.FORBIDDEN);
        }
    }
}
