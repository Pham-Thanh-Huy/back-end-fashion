package com.example.backendfruitable.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private Long orderId;


    private String address;


    private LocalDate createdAt;


    private DeliveryMethodDTO deliveryMethod;



    private PaymentMethodDTO paymentMethod;


    private UserDTO user;


    private List<OrderDetailDTO> orderDetailList;
}
