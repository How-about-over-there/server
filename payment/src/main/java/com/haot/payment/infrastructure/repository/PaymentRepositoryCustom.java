package com.haot.payment.infrastructure.repository;

import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PaymentRepositoryCustom {
    Page<PaymentResponse> searchPayments(String userId, String reservationId, String merchantId,
                                         PaymentMethod method, PaymentStatus status, Double minPrice, Double maxPrice,
                                         LocalDate start, LocalDate end, Pageable pageable);

}
