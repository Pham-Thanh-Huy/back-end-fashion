package com.example.backendfruitable.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {
    private Long imageId;

    @NotNull(message = "Ảnh không được để trống")
    private byte[] data;


    private ProductDTO product;
}
