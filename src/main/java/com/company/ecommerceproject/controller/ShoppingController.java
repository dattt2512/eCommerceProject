package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.dto.response.CustomerDTO;
import com.company.ecommerceproject.dto.response.ProductDTO;
import com.company.ecommerceproject.entities.Order;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.models.CartInfo;
import com.company.ecommerceproject.models.CustomerInfo;
import com.company.ecommerceproject.models.ProductInfo;
import com.company.ecommerceproject.service.OrderService;
import com.company.ecommerceproject.service.impl.ProductServiceImpl;
import com.company.ecommerceproject.ultis.CartUtis;
import com.company.ecommerceproject.validator.CustomerFormValidator;
import com.company.ecommerceproject.validator.ProductFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/shopping")
public class ShoppingController {

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    CustomerFormValidator customerFormValidator;

    @Autowired
    OrderService orderService;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);
        if (target.getClass() == CustomerDTO.class) {
            dataBinder.setValidator(customerFormValidator);
        }
    }

    //Product List Homepage
    @GetMapping("/productList")
    public List<ProductDTO> listAllProduct() {
        return productService.listAll();
    }

    //Buy Product
    @PostMapping("/buyProduct")
    public CartInfo listProductHandler(HttpServletRequest request,
                                       @RequestParam(value = "code", defaultValue = "") String code) {

        Product product = null;
        CartInfo cartInfo = null;

        if (code != null && code.length() > 0) {
            product = productService.findProductByCode(code);
        }

        if (product != null) {
            cartInfo = CartUtis.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.addProduct(productInfo, 1);
        }

        return cartInfo;
    }

    //Remove Product from Shopping Cart
    @PostMapping("/shoppingCartRemoveProduct")
    public CartInfo removeProductHandler(HttpServletRequest request,
                                       @RequestParam(value = "code", defaultValue = "") String code) {

        Product product = null;
        CartInfo cartInfo = null;

        if (code != null && code.length() > 0) {
            product = productService.findProductByCode(code);
        }

        if (product != null) {
            cartInfo = CartUtis.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.removeProduct(productInfo);
        }

        return cartInfo;
    }

    //Update quantity for Product in Cart
    @PostMapping("/shoppingCart")
    public CartInfo shoppingCartUpdateQty(HttpServletRequest request,
                                        @RequestBody CartInfo cartForm) {

        CartInfo cartInfo = CartUtis.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);

        return cartInfo;
    }

    // Show shopping cart
    @GetMapping("/shoppingCart")
    public CartInfo shoppingCartHandler(HttpServletRequest request) {
        CartInfo myCart = CartUtis.getCartInSession(request);
        return myCart;
    }

    //Save customer form
    @PostMapping("/shoppingCartCustomer")
    public CartInfo shoppingCartCustomerSave(HttpServletRequest request,
                                           @RequestBody @Validated CustomerDTO customerDTO) {

        customerDTO.setValid(true);
        CartInfo cartInfo = CartUtis.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerDTO);
        cartInfo.setCustomerInfo(customerInfo);

        return cartInfo;
    }

    // Save shopping Cart
    @PostMapping("/shoppingCartConfirmation")
    public ResponseEntity<?> shoppingCartConfirmationSave(HttpServletRequest request) {
        CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");
        System.out.println(cartInfo.getCustomerInfo().getEmail());

        if (cartInfo.isEmpty()) {
            return new ResponseEntity("Don't have any shopping cart at this time", HttpStatus.FORBIDDEN);
        } else if (!cartInfo.isValidCustomer()) {
            return new ResponseEntity("Your information is not valid", HttpStatus.FORBIDDEN);
        }
        try {
            Order result = orderService.saveOrder(cartInfo);
            System.out.println("orderId: " + result.getId());
        } catch (Exception e) {
            return new ResponseEntity("Can not submit your cart, please try again", HttpStatus.FORBIDDEN);
        }

        // Xóa giỏ hàng khỏi session.
        CartUtis.removeCartInSession(request);

        // Lưu thông tin đơn hàng cuối đã xác nhận mua.
        CartUtis.storeLastOrderedCartInSession(request, cartInfo);

        return new ResponseEntity("Saved your cart successfully", HttpStatus.OK);
    }

    //Get product Image
    @GetMapping("/productImage")
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException {
        Product product = null;
        if (code != null) {
            product = productService.findProductByCode(code);
        }
        if (product != null && product.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.getImage());
        }
        response.getOutputStream().close();
    }
}
