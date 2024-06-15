package com.example.fashion.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {
    private Long imageId;

    private byte[] data;

    private String imageUrl;

    private String imageProduct;

    private ProductDTO product;
}
