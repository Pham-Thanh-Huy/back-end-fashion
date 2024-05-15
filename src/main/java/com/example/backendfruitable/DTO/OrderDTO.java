package com.example.backendfruitable.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private Long orderId;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private String note;

    private LocalDate createdAt;


    private DeliveryMethodDTO deliveryMethod;

    private PaymentMethodDTO paymentMethod;

    private UserDTO user;


    private List<OrderDetailDTO> orderDetailList;
}
