package com.haot.payment.infrastructure.repository;

import com.haot.payment.application.dto.request.PaymentSearchRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepositoryCustom {
    Page<PaymentResponse> searchPayments(PaymentSearchRequest request, Pageable pageable);

}
