package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.Product;
import com.example.backendfruitable.entity.ProductColor;
import com.example.backendfruitable.entity.ProductSize;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
