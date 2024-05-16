package com.example.backendfruitable.repository;

import com.example.backendfruitable.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o from Order o WHERE o.orderId = :orderId")
    Order getOrderByOrderId(@Param("orderId") Long orderId);
}
