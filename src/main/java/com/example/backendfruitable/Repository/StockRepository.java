package com.example.backendfruitable.Repository;

import com.example.backendfruitable.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository  extends JpaRepository<Stock, Long> {
}
