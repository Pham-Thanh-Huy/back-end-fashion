package com.example.fashion.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailDTO {
    private Long orderDetailId;
    private Long quantity;
    private Double totalPrice;

    private ProductDTO product;
    private OrderDTO order;


    private ProductSizeDTO productSize;

    private ProductColorDTO productColor;
}
