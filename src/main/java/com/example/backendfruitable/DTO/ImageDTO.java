package com.example.backendfruitable.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {
    private Long imageId;

    @NotBlank(message = "Dữ liệu ảnh không được để trống")
    private String data;

    private ProductDTO product;
}
