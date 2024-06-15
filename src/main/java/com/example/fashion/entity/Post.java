package com.example.fashion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;



    // tiêu đề bài viết
    @Column(name = "post_title", length = 255)
    private String postTitle;

    // vì bài viết chỉ có 1 ảnh ở đầu nên không cần phải để một bảng riêng và ảnh sẽ ở dạng base64
    @Column(name = "post_image", columnDefinition = "LONGTEXT")
    @Lob
    private String postImage;

    @Column(name = "post_detail", columnDefinition = "LONGTEXT")
    @Lob
    private String postDetail;

    @Column(name = "created_at")
    private LocalDate createdAt;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "category_post_id")
    @JsonIgnore
    private CategoryPost categoryPost;

    // nguời đăng là ai
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "post")
    private List<Comment> commentList;

}
