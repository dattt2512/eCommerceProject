package com.company.ecommerceproject.dto.response;

import com.company.ecommerceproject.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO extends BaseDto<String> implements Serializable {

    private Integer id;

    private String code;

    private String name;

    private Double price;

    private MultipartFile fileData;

    private Integer quantity;
}
