package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.Authorize;
import com.example.backendfruitable.entity.Comment;
import com.example.backendfruitable.entity.Order;
import com.example.backendfruitable.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long userId;
    @NotNull(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Tuổi không được để trống")
    private Integer age;

    @NotNull(message = "Tên không được để trống")
    private String firstName;

    private Boolean isActive;

    @NotNull(message = "Họ tên đệm không được để trống ")
    private String lastName;

    @NotNull(message = "Mật khẩu không được để trống")
    private String password;

    @NotNull(message = "Giới tính không được để trống")
    private char sex;

    @NotNull(message = "Email không được để trống")
    private String email;

    private String tokenActive;

    @NotNull(message = "Tài khoản không được để trống")
    private String username;

    private List<ProductDTO> productList;

    private List<CommentDTO> commentList;

    @NotNull(message =  "Quyền của người dùng không được để trống")
    private List<AuthorizeDTO> authorizeList;

    private List<OrderDTO> orderList;
}
