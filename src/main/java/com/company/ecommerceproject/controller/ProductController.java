package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.dto.CustomerForm;
import com.company.ecommerceproject.dto.ProductFormDTO;
import com.company.ecommerceproject.entities.Order;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.entities.Role;
import com.company.ecommerceproject.entities.User;
import com.company.ecommerceproject.exception.ProductNotFoundException;
import com.company.ecommerceproject.exception.UserNotFoundException;
import com.company.ecommerceproject.models.CartInfo;
import com.company.ecommerceproject.models.CustomerInfo;
import com.company.ecommerceproject.models.ProductInfo;
import com.company.ecommerceproject.repository.OrderRepository;
import com.company.ecommerceproject.service.OrderService;
import com.company.ecommerceproject.service.impl.ProductServiceImpl;
import com.company.ecommerceproject.ultis.CartUltis;
import com.company.ecommerceproject.validator.ProductFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@Transactional
public class ProductController {

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

        // Trường hợp update SL trên giỏ hàng.
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
//        if (target.getClass() == CartInfo.class) {
//
//        }
        if (target.getClass() == ProductFormDTO.class) {
            dataBinder.setValidator(productFormValidator);
        }
    }

    //Product List Manager Page
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

    //Add new product
    @GetMapping("/admin/products/new")
    public String showNewProductForm(Model model) {
        ProductFormDTO productFormDTO = new ProductFormDTO();
        model.addAttribute("pageTitle", "Add New Product");
        model.addAttribute("productFormDTO", productFormDTO);
        return "product_form";
    }

    //Save product
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

    //Edit product
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

    //Delete Product
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

    //Product List Homepage Web Application
    @GetMapping("/productList/page/{pageNum}")
    public String listProductHandler(Model model, @PathVariable("pageNum") int pageNum) {

        Page<Product> page = productService.listAll(pageNum);
        List<Product> productList = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalProducts", page.getTotalElements());

        model.addAttribute("productList", productList);
        return "productList";
    }

    //Buy Product
    @GetMapping("/buyProduct")
    public String listProductHandler(HttpServletRequest request, Model model, //
                                     @RequestParam(value = "code", defaultValue = "") String code) {

        Product product = null;
        if (code != null && code.length() > 0) {
            product = productService.findProduct(code);
        }
        if (product != null) {

            //
            CartInfo cartInfo = CartUltis.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.addProduct(productInfo, 1);
        }

        return "redirect:/shoppingCart";
    }

    //Remove Product from Shopping Cart
    @RequestMapping("/shoppingCartRemoveProduct")
    public String removeProductHandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productService.findProduct(code);
        }
        if (product != null) {

            CartInfo cartInfo = CartUltis.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.removeProduct(productInfo);

        }

        return "redirect:/shoppingCart";
    }

    //Update quantity for Product in Cart
    @PostMapping("/shoppingCart")
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                                        Model model, //
                                        @ModelAttribute("cartForm") CartInfo cartForm) {

        CartInfo cartInfo = CartUltis.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);

        return "redirect:/shoppingCart";
    }

    // Show shopping cart
    @GetMapping("/shoppingCart")
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = CartUltis.getCartInSession(request);

        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    //Show shopping cart customer form
    @GetMapping("/shoppingCartCustomer")
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {

        CartInfo cartInfo = CartUltis.getCartInSession(request);

        if (cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        }
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();

        CustomerForm customerForm = new CustomerForm(customerInfo);

        model.addAttribute("customerForm", customerForm);

        return "shoppingCartCustomer";
    }

    //Save customer form
    @PostMapping("/shoppingCartCustomer")
    public String shoppingCartCustomerSave(HttpServletRequest request, //
                                           Model model, //
                                           @ModelAttribute("customerForm") @Validated CustomerForm customerForm, //
                                           BindingResult result, //
                                           final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            customerForm.setValid(false);
            // Forward tới trang nhập lại.
            return "shoppingCartCustomer";
        }

        customerForm.setValid(true);
        CartInfo cartInfo = CartUltis.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);

        return "redirect:/shoppingCartConfirmation";
    }

    //shopping Cart Confirmation Review
    @GetMapping("/shoppingCartConfirmation")
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = CartUltis.getCartInSession(request);

        if (cartInfo == null || cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {

            return "redirect:/shoppingCartCustomer";
        }
        model.addAttribute("myCart", cartInfo);
        request.getSession().setAttribute("myCart", cartInfo);

        return "shoppingCartConfirmation";
    }

    // Save shopping Cart
    @RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.POST)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");
        System.out.println(cartInfo.getCustomerInfo().getEmail());
        model.addAttribute("myCart", cartInfo);

        if (cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {

            return "redirect:/shoppingCartCustomer";
        }
        try {
            Order result = orderService.saveOrder(cartInfo);
            System.out.println("orderId: "+result.getId());
        } catch (Exception e) {
            return "shoppingCartConfirmation";
        }

        // Xóa giỏ hàng khỏi session.
        CartUltis.removeCartInSession(request);

        // Lưu thông tin đơn hàng cuối đã xác nhận mua.
        CartUltis.storeLastOrderedCartInSession(request, cartInfo);

        return "redirect:/shoppingCartFinalize";
    }

    //Finalize Shopping Cart
    @RequestMapping(value = {"/shoppingCartFinalize"}, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {

        CartInfo lastOrderedCart = CartUltis.getLastOrderedCartInSession(request);

        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }

    //Get product Image
    @GetMapping("/productImage")
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException {
        Product product = null;
        if (code != null) {
            product = productService.findProduct(code);
        }
        if (product != null && product.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.getImage());
        }
        response.getOutputStream().close();
    }
}
