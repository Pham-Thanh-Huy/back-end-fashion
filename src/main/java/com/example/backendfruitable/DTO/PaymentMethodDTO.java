package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.Order;
import com.example.backendfruitable.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethodDTO {
    private Long paymentId;

    private String paymentName;

    private String description;

    private Double paymentCost;

    private List<OrderDTO> orderList;

    private UserDTO user;
}
