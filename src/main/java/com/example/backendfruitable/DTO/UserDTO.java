package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.CategoryPost;
import com.example.backendfruitable.entity.DeliveryMethod;
import com.example.backendfruitable.entity.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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

    private String password;

    @NotNull(message = "Giới tính không được để trống")
    private Character sex;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    private String userImage;

    private byte[] dataImage;

    private String imageUrl;

    private String tokenActive;

    @NotBlank(message = "Tài khoản không được để trống")
    private String username;

    private List<ProductDTO> productList;

    private List<CommentDTO> commentList;

    @NotNull(message =  "Quyền của người dùng không được để trống")
    private List<AuthorizeDTO> authorizeList;

    private List<PostDTO> postList;

    // json ignore bên entity
    private List<CategoryPostDTO> categoryPostList;

    //json ignore bên entity
    private List<CategoryProductDTO> categoryProductList;

    //Start json Ignore
    private List<DeliveryMethodDTO> deliveryMethodList;

    private List<PaymentMethodDTO> paymentMethodList;

    // End JsonIgnore

}
