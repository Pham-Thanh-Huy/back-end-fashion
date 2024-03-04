package com.example.backendfruitable.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {
    private Long imageId;

    @NotEmpty(message = "Dữ liệu ảnh không được để trống")
    private String data;

    private ProductDTO product;
}
