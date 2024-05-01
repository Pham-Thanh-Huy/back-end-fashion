package com.example.backendfruitable.DTO;


import lombok.Data;

@Data
public class CommentDTO {
    private Long commentId;
    private String content;


    private ProductDTO product;

    private UserDTO user;

    private PostDTO post;
}
