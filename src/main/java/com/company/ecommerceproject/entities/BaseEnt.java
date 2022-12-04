package com.company.ecommerceproject.entities;

import com.company.ecommerceproject.service.dto.BaseDTO;
import lombok.Data;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class BaseEnt<T> implements Serializable {

    private String uuid;

    private Date createdDate;

    private Date updatedDate;

    /*
     *Get Ent to Dto object
     * */
    public abstract BaseDTO getAsDto();
}
