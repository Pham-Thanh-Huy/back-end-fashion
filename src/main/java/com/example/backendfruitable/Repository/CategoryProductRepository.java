package com.example.backendfruitable.Repository;

import com.example.backendfruitable.entity.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryProductRepository  extends JpaRepository<CategoryProduct, Long> {
    @Query("SELECT c FROM CategoryProduct c WHERE  c.categoryId= :id")
    public CategoryProduct getCategoryProductById(@Param("id") Long id);
}
