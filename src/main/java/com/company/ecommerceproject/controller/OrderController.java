package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.entities.Order;
import com.company.ecommerceproject.models.OrderDetailInfo;
import com.company.ecommerceproject.models.OrderInfo;
import com.company.ecommerceproject.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    //Trang liệt kê danh sách order
    @GetMapping("/admin/orderList/page/{pageNum}")
    public String ordersList(Model model, @PathVariable("pageNum") int pageNum, Principal principal) {
        String username = principal.getName();
        System.out.println("Username: " + username);
        Page<Order> page = orderService.listAll(pageNum);
        List<Order> ordersList = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalOrders", page.getTotalElements());
        model.addAttribute("ordersList", ordersList);

        return "ordersList";
    }

    //Trang view chi tiết 1 order
    @GetMapping("/admin/order")
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = orderService.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/admin/orderList/page/1";
        }
        List<OrderDetailInfo> details = orderService.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);

        model.addAttribute("orderInfo", orderInfo);

        return "order";
    }
}
