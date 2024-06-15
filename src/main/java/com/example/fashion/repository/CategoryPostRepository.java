package com.example.fashion.repository;

import com.example.fashion.entity.CategoryPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryPostRepository extends JpaRepository<CategoryPost, Long> {
    @Query("SELECT c FROM CategoryPost c WHERE c.categoryId = :categoryId")
    CategoryPost findCategoryPostByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT c FROM CategoryPost  c WHERE c.parentId = :parentId")
    List<CategoryPost> getListCategoryPostByParentId(@Param("parentId") Long parentId);
}
