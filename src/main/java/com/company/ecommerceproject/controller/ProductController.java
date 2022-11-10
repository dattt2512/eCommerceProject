package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.dto.ProductFormDTO;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.exception.ProductNotFoundException;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.service.impl.ProductServiceImpl;
import com.company.ecommerceproject.validator.ProductFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    ProductFormValidator productFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        // Trường hợp update SL trên giỏ hàng.
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
//        if (target.getClass() == CartInfo.class) {
//
//        }
        if (target.getClass() == ProductFormDTO.class) {
            dataBinder.setValidator(productFormValidator);
        }
    }

    @GetMapping("/admin/products/page/{pageNum}")
    public String showProductPage(Model model, @PathVariable("pageNum") int pageNum, Principal principal) {
        String username = principal.getName();
        System.out.println("Username: " + username);
        Page<Product> page = productService.listAll(pageNum);
        List<Product> productsList = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalProducts", page.getTotalElements());
        model.addAttribute("productsList", productsList);

        return "products";
    }

    @GetMapping("/admin/products/new")
    public String showNewProductForm(Model model) {
        ProductFormDTO productFormDTO = new ProductFormDTO();
        model.addAttribute("pageTitle", "Add New Product");
        model.addAttribute("productFormDTO", productFormDTO);
        return "product_form";
    }

    @PostMapping("/admin/products/save")
    public String saveProduct(Model model,
                              @ModelAttribute("productFormDTO") @Validated ProductFormDTO productFormDTO,
                              BindingResult result,
                              RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "product_form";
        }
        try {
            productService.save(productFormDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "product_form";
        }
        ra.addFlashAttribute("message", "Product has been saved succesfully.");
        return "redirect:/admin/products/page/1";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id")Integer id, Model model, RedirectAttributes ra) {
        try {
            Product product = productService.getProductById(id);
            ProductFormDTO productFormDTO = new ProductFormDTO(product);
            model.addAttribute("productFormDTO", productFormDTO);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            return "product_form";
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/users/page/1";
        }
    }

    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            productService.delete(id);
            ra.addFlashAttribute("message", "User ID "+id+ " has been deleted.");
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/products/page/1";
    }
}
