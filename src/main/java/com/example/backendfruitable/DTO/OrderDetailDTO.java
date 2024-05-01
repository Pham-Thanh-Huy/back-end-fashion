package com.example.backendfruitable.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailDTO {

    private Long deliveryId;


    private String name;


    private String description;


    private Double deliveryCost;


    private List<OrderDTO> order;

    private ProductDTO product;

    private UserDTO user;
}
