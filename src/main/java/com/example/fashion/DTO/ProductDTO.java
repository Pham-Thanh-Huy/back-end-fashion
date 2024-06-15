package com.example.fashion.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    private Long productId;

    @NotNull(message = "Giá niêm yết không được để trống")
    private Double listedPrice;

    @NotNull(message = "Lựa chọn sản phẩm nổi bật không được để trống")
    private Boolean outstanding;

    @NotBlank(message = "Code của sản phẩm không được để trống")
    private String productCode;

    @NotBlank(message = "Mô tả sản phẩm không được để trống")
    private String productDescription;

    @NotBlank(message = "Chi tiết sản phẩm không được để trống")
    private String productDetail;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

    @NotNull(message = "Giá tiền của sản phẩm không được để trống")
    private Double productPrice;

    private LocalDate createdAt;

    @NotNull(message = "Ảnh của sản phẩm không được để trống")
    private List<ImageDTO> imageList;

    private CategoryProductDTO categoryProduct;

    private List<CommentDTO> commentList;

    private List<InventoryDTO> inventoryList;


    private UserDTO user;

//    JsonIgnore
    private List<OrderDetailDTO> orderDetail;
}
