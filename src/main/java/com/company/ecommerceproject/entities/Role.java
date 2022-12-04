package com.company.ecommerceproject.entities;

import com.company.ecommerceproject.service.dto.BaseDTO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEnt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public BaseDTO getAsDto() {
        return null;
    }
}
