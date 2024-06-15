package com.example.fashion.repository;

import com.example.fashion.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.productId = :id")
    Product getProductById(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE p.categoryProduct.categoryId IN :categoryIds ORDER BY p.productId DESC")
    List<Product> getListProductByCategoryProductId(@Param("categoryIds") List<Long> categoryIds);



}
