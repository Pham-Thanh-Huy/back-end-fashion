package com.example.fashion.repository;

import com.example.fashion.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o from Order o WHERE o.orderId = :orderId")
    Order getOrderByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT o from Order o WHERE o.status = :status")
    Page<Order> getOrderByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT o FROM Order  o WHERE o.status <>  :status")
    List<Order> getOrderByStatusNot(@Param("status") String status);

    @Query("SELECT o from Order o WHERE o.status = :status")
    List<Order> getOrderByStatus(@Param("status") String status);

}
