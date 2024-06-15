package com.example.fashion.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductColorDTO {
    private Integer productColorId;

    @NotBlank(message = "Tên màu sản phẩm không được để trống")
    private String colorName;

    //    ---- JsonIgnore--------
    private List<InventoryDTO> inventoryList;
    private List<OrderDetailDTO> orderDetailList;
    //    ---- END JsonIgnore--------
}
