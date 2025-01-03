package com.haot.payment.infrastructure.repository;

import com.haot.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
