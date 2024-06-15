package com.example.fashion.repository;

import com.example.fashion.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, Integer> {
    @Query("SELECT pc FROM ProductColor pc WHERE pc.productColorId = :id")
    ProductColor findProductColorByProductColorId(@Param("id") int productColorId);
}
