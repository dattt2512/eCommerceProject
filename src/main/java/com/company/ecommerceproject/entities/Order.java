package com.company.ecommerceproject.entities;

import com.company.ecommerceproject.service.dto.BaseDTO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "orders", uniqueConstraints = {@UniqueConstraint(columnNames = "order_num")})
@Data
public class Order extends BaseEnt implements Serializable {

    private static final long serialVersionUID = -2576670215015463100L;

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "order_num", nullable = false)
    private int orderNum;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "customer_name", length = 255, nullable = false)
    private String customerName;

    @Column(name = "customer_address", length = 255, nullable = false)
    private String customerAddress;

    @Column(name = "customer_email", length = 128, nullable = false)
    private String customerEmail;

    @Column(name = "customer_phone", length = 128, nullable = false)
    private String customerPhone;

    @Override
    public BaseDTO getAsDto() {
        return null;
    }
}
