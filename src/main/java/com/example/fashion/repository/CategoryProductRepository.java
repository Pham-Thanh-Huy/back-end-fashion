package com.example.fashion.repository;

import com.example.fashion.entity.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryProductRepository  extends JpaRepository<CategoryProduct, Long> {
    @Query("SELECT c FROM CategoryProduct c WHERE  c.categoryId= :id")
     CategoryProduct getCategoryProductById(@Param("id") Long id);

    @Query("SELECT c FROM  CategoryProduct c ORDER BY c.categoryId DESC ")
    List<CategoryProduct> getAllCategoryProduct();

    @Query("SELECT c FROM CategoryProduct  c WHERE c.categoryName = :categoryName")
    List<CategoryProduct> getCategoryProductByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT c FROM CategoryProduct  c WHERE c.parentId= :parenId")
    List<CategoryProduct> getCategoryProductByParentId(@Param("parenId") Long parentId);



}
