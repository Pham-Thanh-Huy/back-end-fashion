package com.example.fashion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    @Column(name = "first_name", length = 32)
    private String firstName;

    @Column(name = "last_name", length = 32)
    private String lastname;

    @Column(name = "sex")
    private char sex;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "age")
    private int age;

    @Column(name = "username", length = 32)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "user_image", columnDefinition = "LONGTEXT")
    @Lob
    private String userImage;

    @Column(name = "token_active", length = 255)
    private String token_active;

    @Column(name = "is_active")
    private Boolean isActive;


    // list sản phẩm đã add có thể null vì nếu không phải admin thì không có quyền add
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Product> productList;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Comment> commentList;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_authorize", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "authorize_id"))
    private List<Authorize> authorizeList;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Order> orderList;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Post> postList;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH},
    mappedBy = "user")
    @JsonIgnore
    private List<CategoryProduct> categoryProductList;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "user")
    @JsonIgnore
    private List<CategoryPost> categoryPostList;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "user")
    @JsonIgnore
    private List<DeliveryMethod> deliveryMethodList;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "user")
    @JsonIgnore
    private List<PaymentMethod> paymentMethodList;


}
