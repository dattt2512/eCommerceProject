package com.company.ecommerceproject.service.impl;

import com.company.ecommerceproject.entities.Order;
import com.company.ecommerceproject.entities.OrderDetails;
import com.company.ecommerceproject.entities.Product;
import com.company.ecommerceproject.models.*;
import com.company.ecommerceproject.repository.OrderDetailsRepository;
import com.company.ecommerceproject.repository.OrderRepository;
import com.company.ecommerceproject.repository.ProductRepository;
import com.company.ecommerceproject.service.OrderService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderDetailsRepository orderDetailsRepo;

    @Override
    public Page<Order> listAll(int pageNum) {
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return orderRepo.findAll(pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order saveOrder(CartInfo cartInfo) {

        int orderNum = orderRepo.getMaxOrderNum() + 1;
        Order order = new Order();

        order.setId((UUID.randomUUID().toString()));
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());
        order.setCustomerAddress(customerInfo.getAddress());

        orderRepo.save(order);

        List<CartLineInfo> lines = cartInfo.getCartLines();

        for (CartLineInfo line: lines) {
            OrderDetails details = new OrderDetails();
            details.setId(UUID.randomUUID().toString());
            details.setOrder(order);
            details.setAmount(line.getAmount());
            details.setPrice(line.getProductInfo().getPrice());
            details.setQuantity(line.getQuantity());

            String code = line.getProductInfo().getCode();
            Product product = productRepo.findProduct(code);
            details.setProduct(product);

            orderDetailsRepo.save(details);
        }

        cartInfo.setOrderNum(orderNum);

        return order;
    }

    @Override
    public Order findOrder(String orderId) {
        return orderRepo.findById(orderId).get();
    }

    @Override
    public OrderInfo getOrderInfo(String orderId) {
        Order order = orderRepo.findById(orderId).get();
        if (order == null) {
            return null;
        }
        return new OrderInfo(order.getId(), order.getOrderDate(),
                order.getOrderNum(), order.getAmount(), order.getCustomerName(),
                order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone());
    }

    @Override
    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
        List<OrderDetailInfo> list = new ArrayList<>();
        Order order = orderRepo.findById(orderId).get();

        List<OrderDetails> orderDetails = orderRepo.listAllOrderDetailsByOrder(order);
        for (OrderDetails od : orderDetails) {
            OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
            orderDetailInfo.setId(od.getId());
            orderDetailInfo.setAmount(od.getAmount());
            orderDetailInfo.setPrice(od.getPrice());
            orderDetailInfo.setQuantity(od.getQuantity());
            orderDetailInfo.setProductCode(od.getProduct().getCode());
            orderDetailInfo.setProductName(od.getProduct().getName());

            list.add(orderDetailInfo);
        }
        return list;
    }
}
