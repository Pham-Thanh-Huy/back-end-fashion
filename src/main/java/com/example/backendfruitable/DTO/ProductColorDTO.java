package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductColorDTO {
    private Integer productColorId;

    private String colorName;

    //    ---- JsonIgnore--------
    private List<InventoryDTO> inventoryList;
    private List<OrderDetailDTO> orderDetailList;
    //    ---- END JsonIgnore--------
}
