package com.example.fashion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "product_size")
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_size_id")
    private Integer productSizeId;

    @Column(name = "size_name", length = 50)
    private String sizeName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL
            , mappedBy = "productSize")
    @JsonIgnore
    private List<Inventory> inventoryList;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH}
            , mappedBy = "productSize")
    @JsonIgnore
    private List<OrderDetail> orderDetailList;


}
