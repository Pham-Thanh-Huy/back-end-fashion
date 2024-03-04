package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockDTO {
    private Long stockId;
    private Long quantity;

    private ProductDTO product;
}
