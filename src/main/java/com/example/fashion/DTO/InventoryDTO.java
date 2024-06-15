package com.example.fashion.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryDTO {
    private Long inventoryId;
    private Long quantity;

    private ProductDTO product;
    private ProductSizeDTO productSize;
    private ProductColorDTO productColor;
}
