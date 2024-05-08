package com.example.backendfruitable.repository;

import com.example.backendfruitable.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    @Query("SELECT PM FROM PaymentMethod PM WHERE PM.paymentId = :paymentId")
    PaymentMethod findPaymentMethodById(Long paymentId);
}
