package com.example.fashion.repository;

import com.example.fashion.entity.DeliveryMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Long> {
    @Query("SELECT dm FROM DeliveryMethod dm WHERE dm.deliveryId = :id")
    DeliveryMethod findDeliveryMethodByDeliveryId(@Param("id") Long id);
}
