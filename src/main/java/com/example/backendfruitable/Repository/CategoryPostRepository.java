package com.example.backendfruitable.Repository;

import com.example.backendfruitable.entity.CategoryPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryPostRepository extends JpaRepository<CategoryPost, Long> {
    @Query("SELECT c FROM CategoryPost c WHERE c.categoryId = :categoryId")
    CategoryPost findCategoryPostByCategoryId(@Param("categoryId") Long categoryId);
}
