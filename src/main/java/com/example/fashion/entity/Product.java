package com.example.fashion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    // code sản phẩm
    @Column(name = "product_code", length = 50)
    private String productCode;

    @Column(name = "product_name", length = 255)
    private String productName;

    @Column(name = "listed_price")
    private Double listedPrice;

    @Column(name = "product_price")
    private Double productPrice;

    // Chi tiết sản phẩm
    @Column(name = "product_detail", columnDefinition = "TEXT")
    private String productDetail;

    // Mô tả sản phẩm
    @Column(name = "product_description", columnDefinition = "LONGTEXT")
    @Lob
    private String productDescription;

    @Column(name = "outstanding")
    private Boolean outstanding;

    // ngày thêm sản phẩm
    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<ImageProduct> imageList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<Comment> commentList;

    // số lượng sản phẩm còn lại trong cửa hàng
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<Inventory> inventoryList;

    // Danh mục
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "category_product_id")
    private CategoryProduct categoryProduct;

    // người nào thêm sản phẩm
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "product")
    @JsonIgnore
    private List<OrderDetail> orderDetails;
}
