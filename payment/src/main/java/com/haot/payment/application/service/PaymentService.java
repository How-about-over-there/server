package com.haot.payment.application.service;

import com.haot.payment.application.dto.request.PaymentCancelRequest;
import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.request.PaymentSearchRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PaymentService {

    // 결제 생성
    PaymentResponse createPayment(PaymentCreateRequest request);

    // 결제 확인
    PaymentResponse completePayment(String paymentId);

    // 본인 결제 단건 조회
    PaymentResponse getPaymentById(String paymentId);

    // 결제 취소 요청
    PaymentResponse cancelPayment(PaymentCancelRequest request, String paymentId);

    // 본인 결제 전체 조회 및 검색
    Page<PaymentResponse> getPayments(PaymentSearchRequest request, Pageable pageable);
}
