package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private Long postId;
    private String postTitle;
    private String postImage;
    private String postDetail;
    private LocalDate createdAt;


    private CategoryPostDTO categoryPostDTO;
    private UserDTO userDTO;
    private List<CommentDTO> commentDTOList;

}
