package com.example.fashion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "product_color")
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_color_id")
    private Integer productColorId;

    @Column(name = "color_name")
    private String colorName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL
            , mappedBy = "productColor")
    @JsonIgnore
    private List<Inventory> inventoryList;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}
    , mappedBy = "productColor")
    @JsonIgnore
    private List<OrderDetail> orderDetailList;

}
