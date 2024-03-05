package com.example.backendfruitable.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "category_post")
public class CategoryPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "parent_id")
    private Long parentId;


    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    , mappedBy = "categoryPost")
    private List<Post> postList;
}
