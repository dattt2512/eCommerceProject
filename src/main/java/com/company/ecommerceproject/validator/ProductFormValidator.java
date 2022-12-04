package com.company.ecommerceproject.validator;

import com.company.ecommerceproject.service.dto.ProductFormDTO;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProductFormValidator implements Validator {
    @Autowired
    private ProductRepository productRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ProductFormDTO.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductFormDTO productFormDTO = (ProductFormDTO) target;

        // Kiểm tra các trường (field) của ProductForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.productForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.productForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.productForm.price");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "NotEmpty.productForm.quantity");

        String code = productFormDTO.getCode();
        if (code != null && code.length() > 0) {
            if (code.matches("\\s+")) {
                errors.rejectValue("code", "Pattern.productForm.code");
            } else if (productFormDTO.isNewProduct()) {
                Product product = productRepo.findProduct(code);
                if (product != null) {
                    errors.rejectValue("code", "Duplicate.productForm.code");
                }
            }
        }
    }
}
