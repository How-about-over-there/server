package com.haot.payment.application.service;

import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import com.haot.payment.domain.model.Payment;
import com.haot.payment.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "PaymentServiceImpl")
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentCreateRequest request) {

        // TODO: 1. 요청 데이터 유효성 검사

        // 2. 결제 정보 저장
        Payment payment = Payment.create(
                request.userId(),
                request.reservationId(),
                request.price(),
                PaymentMethod.fromString(request.method()),
                PaymentStatus.READY
        );
        paymentRepository.save(payment);
        return PaymentResponse.of(payment);
    }
}
