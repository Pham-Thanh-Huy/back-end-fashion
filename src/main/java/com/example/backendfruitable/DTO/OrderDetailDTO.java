package com.example.backendfruitable.DTO;


import com.example.backendfruitable.entity.Order;
import com.example.backendfruitable.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailDTO {
    private Long orderDetailId;
    private Long quantity;
    private Double totalPrice;

    private ProductDTO product;
    private OrderDTO order;
}
