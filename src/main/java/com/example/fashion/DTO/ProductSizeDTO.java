package com.example.fashion.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSizeDTO {
    private Integer productSizeId;

    @NotBlank(message = "Tên size không được để trống")
    private String sizeName;

    //    ---- JsonIgnore--------
    private List<InventoryDTO> inventoryList;
    private List<OrderDetailDTO> orderDetailList;
    //    ---- END JsonIgnore--------
}
