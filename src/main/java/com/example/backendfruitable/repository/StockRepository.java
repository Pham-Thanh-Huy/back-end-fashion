package com.example.backendfruitable.repository;

import com.example.backendfruitable.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository  extends JpaRepository<Stock, Long> {
}
