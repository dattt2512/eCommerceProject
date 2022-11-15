package com.company.ecommerceproject.service;

import com.company.ecommerceproject.entities.Order;
import com.company.ecommerceproject.models.CartInfo;
import com.company.ecommerceproject.models.OrderDetailInfo;
import com.company.ecommerceproject.models.OrderInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    Page<Order> listAll(int pageNum);

    public Order saveOrder(CartInfo cartInfo);

    public Order findOrder(String orderId);

    OrderInfo getOrderInfo(String orderId);

    List<OrderDetailInfo> listOrderDetailInfos(String orderId);
}
