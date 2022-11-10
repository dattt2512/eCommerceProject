package com.company.ecommerceproject.dto;

import com.company.ecommerceproject.entities.Product;
import org.springframework.web.multipart.MultipartFile;

public class ProductFormDTO {
    private String code;
    private String name;
    private Double price;
    private MultipartFile fileData;
    private Integer quantity;
    private boolean newProduct = false;

    public ProductFormDTO() {
        this.newProduct = true;
    }

    public ProductFormDTO(Product product) {
        this.code = product.getCode();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.newProduct = false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isNewProduct() {
        return newProduct;
    }

    public void setNewProduct(boolean newProduct) {
        this.newProduct = newProduct;
    }
}
