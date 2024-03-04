package com.example.backendfruitable.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryProductDTO {
    private Long categoryId;
    @NotNull(message = "Tên danh mục sản phẩm không được để trống")
    private String categoryName;

    private List<ProductDTO> productList;
}