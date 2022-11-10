package com.company.ecommerceproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping("/admin/products")
    public String showProductPage() {
        return "products";
    }
}
