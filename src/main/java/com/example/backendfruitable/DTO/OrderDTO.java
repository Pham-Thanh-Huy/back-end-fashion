package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.Product;
import com.example.backendfruitable.entity.User;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;
    private String orderCode;

    private List<ProductDTO> productList;

    private UserDTO user;
}
