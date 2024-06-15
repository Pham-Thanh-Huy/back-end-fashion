package com.example.fashion.repository;

import com.example.fashion.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Integer> {
        @Query("SELECT pz FROM ProductSize pz where pz.productSizeId = :id")
        ProductSize findProductSizeByProductSizeId(@Param("id") int productSizeId);
}
