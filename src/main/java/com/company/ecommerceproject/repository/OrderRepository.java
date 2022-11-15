package com.company.ecommerceproject.repository;

import com.company.ecommerceproject.entities.Order;
import com.company.ecommerceproject.entities.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query(value = "SELECT od from OrderDetails od where od.order = ?1")
    public List<OrderDetails> listAllOrderDetailsByOrder(Order order);

    @Query(value = "SELECT max (o.orderNum) from Order o")
    public int getMaxOrderNum();
}
