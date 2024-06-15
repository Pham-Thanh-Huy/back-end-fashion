package com.example.fashion.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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
    private String status;
    private LocalDate createdAt;

    // các trường tự tạo thêm phục vụ xử lý dto
    //đây là trường tổng tiền cho mỗi đơn hàng hiện tại
    private Double totalPrice;

    // đây là trường tính tổng tiền cho toàn bộ tất cả đơn hàng trong hệ thống;
    private Double grandTotalPrice;


    // các relation ship
    private DeliveryMethodDTO deliveryMethod;

    private PaymentMethodDTO paymentMethod;

    private UserDTO user;


    private List<OrderDetailDTO> orderDetailList;
}
