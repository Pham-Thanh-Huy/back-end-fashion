package com.example.backendfruitable.Repository;

import com.example.backendfruitable.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
     @Query("SELECT p FROM Post p WHERE p.postId = :postId")
     Post findPostByPostId(@Param("postId") Long postId);
}
