package com.haot.payment.application.service;

import com.haot.payment.application.dto.request.PaymentCancelRequest;
import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.request.PaymentSearchRequest;
import com.haot.payment.application.dto.response.PageResponse;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    // 결제 생성
    PaymentResponse createPayment(PaymentCreateRequest request, String userId, Role role);

    // 결제 확인
    PaymentResponse completePayment(String paymentId, String userId, Role role);

    // 본인 결제 단건 조회
    PaymentResponse getPaymentById(String paymentId, String userId, Role role);

    // 결제 취소 요청
    PaymentResponse cancelPayment(PaymentCancelRequest request, String paymentId, String userId, Role role);

    // 본인 결제 전체 조회 및 검색
    PageResponse<PaymentResponse> getPayments(PaymentSearchRequest request, Pageable pageable, String userId, Role role);
}
