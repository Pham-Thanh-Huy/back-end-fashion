package com.example.backendfruitable.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryPostDTO {
    private Long categoryId;
    private String categoryName;
    private Long parentId;


    private List<PostDTO> postList;
}
