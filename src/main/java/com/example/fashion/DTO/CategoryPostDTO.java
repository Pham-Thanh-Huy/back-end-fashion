package com.example.fashion.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryPostDTO {
    private Long categoryId;
    @NotBlank(message = "Tên danh mục bài viết không được để trống")
    private String categoryName;
    @NotNull(message = "Danh mục cha không được để trống nếu không có danh mục cha nào hãy để nó là 0")
    private Long parentId;
    private LocalDate createdAt;

    private UserDTO user;
    private List<PostDTO> postList;
}
