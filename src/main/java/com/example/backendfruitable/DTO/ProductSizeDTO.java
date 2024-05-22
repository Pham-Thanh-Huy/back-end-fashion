package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.Inventory;
import com.example.backendfruitable.entity.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSizeDTO {
    private Integer productSizeId;

    private String sizeName;

    //    ---- JsonIgnore--------
    private List<InventoryDTO> inventoryList;
    private List<OrderDetailDTO> orderDetailList;
    //    ---- END JsonIgnore--------
}
