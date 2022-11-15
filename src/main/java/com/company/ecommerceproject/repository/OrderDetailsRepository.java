package com.company.ecommerceproject.repository;

import com.company.ecommerceproject.entities.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, String> {
}
