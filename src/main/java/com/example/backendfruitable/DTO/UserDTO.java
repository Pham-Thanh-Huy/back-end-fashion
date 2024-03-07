package com.example.backendfruitable.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; // Thay thế @NotNull bằng @NotBlank
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long userId;
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Tuổi không được để trống")
    private Integer age;

    @NotBlank(message = "Tên không được để trống")
    private String firstName;

    private Boolean isActive;

    @NotBlank(message = "Họ tên đệm không được để trống ")
    private String lastName;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotNull(message = "Giới tính không được để trống")
    private Character sex;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Ảnh có user không được để trống nếu trống hãy thêm ảnh mặc định")
    private String userImage;

    private String tokenActive;

    @NotBlank(message = "Tài khoản không được để trống")
    private String username;

    private List<ProductDTO> productList;

    private List<CommentDTO> commentList;

    @NotNull(message =  "Quyền của người dùng không được để trống")
    private List<AuthorizeDTO> authorizeList;

    private List<OrderDTO> orderList;

    private List<PostDTO> postList;
}
