package com.example.fashion.repository;

import com.example.fashion.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
     @Query("SELECT p FROM Post p WHERE p.postId = :postId")
     Post findPostByPostId(@Param("postId") Long postId);

     @Query("SELECT p FROM Post p WHERE p.categoryPost.categoryId IN :categoryIds ORDER BY p.postId DESC")
     List<Post> findPostByCategoryPostId(@Param("categoryIds") List<Long> categoryIds);

}
