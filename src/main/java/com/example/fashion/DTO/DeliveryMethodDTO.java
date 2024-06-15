package com.example.fashion.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryMethodDTO {
    private Long deliveryId;

    @NotBlank(message = "Phương thức vận chuyện không được để trống")
    private String name;

    @NotBlank(message = "Mô tả chi tiết sản phẩm không được để trống")
    private String description;

    @NotNull(message = "Giá tiền cho phương thức vận chuyển khong được để trống")
    private Double deliveryCost;



    private List<OrderDTO> order;
    private UserDTO user;
}
