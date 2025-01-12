package com.haot.payment.infrastructure.repository;

import com.haot.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String>, PaymentRepositoryCustom{
    Optional<Payment> findByIdAndIsDeletedFalse(String paymentId);
}
