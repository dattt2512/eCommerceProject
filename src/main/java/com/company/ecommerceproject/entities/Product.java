package com.company.ecommerceproject.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product extends BaseEnt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Lob
    @Column(length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = true)
    private LocalDateTime deletedDate;
}
