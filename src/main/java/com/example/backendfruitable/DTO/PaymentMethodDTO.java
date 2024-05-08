package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.Order;
import com.example.backendfruitable.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethodDTO {
    private Long paymentId;

    @NotBlank(message = "Tên phương thức thanh toán không được để trống")
    private String paymentName;

    @NotBlank(message = "Mô tả phương thức thanh toán không được để trống")
    private String description;

    @NotNull(message = "Chi phí phương thức thanh toán không được để trống")
    private Double paymentCost;

    private List<OrderDTO> orderList;

    private UserDTO user;
}
