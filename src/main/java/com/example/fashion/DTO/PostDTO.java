package com.example.fashion.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private Long postId;
    @NotBlank(message = "Tiêu đề bài viết không được để trống")
    private String postTitle;
    // byte của imgae để xử lý
    private byte[] dataImage;

    private String postImage;
    private String imageUrl;

    @NotBlank(message = "Chi tiết bài viết không được để trống")
    private String postDetail;

    private LocalDate createdAt;


    private CategoryPostDTO categoryPost;
    private UserDTO user;
    private List<CommentDTO> commentDTOList;

}
