package com.example.backendfruitable.Repository;
import com.example.backendfruitable.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageProduct, Long> {
}
