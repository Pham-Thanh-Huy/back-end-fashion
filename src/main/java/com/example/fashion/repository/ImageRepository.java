package com.example.fashion.repository;
import com.example.fashion.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageProduct, Long> {
}
