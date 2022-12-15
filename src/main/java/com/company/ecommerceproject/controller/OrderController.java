package com.company.ecommerceproject.controller;

import com.company.ecommerceproject.entities.Order;
import com.company.ecommerceproject.models.OrderInfo;
import com.company.ecommerceproject.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/admin/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping
    public List<Order> ordersList() {
        return orderService.listAll();
    }

    //Trang view chi tiết 1 order
    @GetMapping("/{orderId}")
    public OrderInfo orderView(@PathVariable("orderId") String orderId) {
        return orderService.getOrderInfo(orderId);
    }
}
