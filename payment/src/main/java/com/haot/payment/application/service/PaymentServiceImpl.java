package com.haot.payment.application.service;

import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import com.haot.payment.domain.model.Payment;
import com.haot.payment.infrastructure.client.PortOneService;
import com.haot.payment.infrastructure.client.dto.PortOneResponse;
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
    private final PortOneService portOneService;

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

    @Override
    @Transactional
    public PaymentResponse completePayment(String paymentId) {
        // 1. 결제 데이터 확인
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomPaymentException(ErrorCode.PAYMENT_NOT_FOUND));

        // 2. 포트원 결제 단건 조회 API 호출
        PortOneResponse paymentData = portOneService.getPaymentData(payment.getId());
        log.info("포트원 결제 정보 ::::: {}", paymentData);

        PaymentStatus status = PaymentStatus.fromString(paymentData.status());   // 결제 상태
        Double finalPrice = paymentData.amount().total().doubleValue(); // 결제 금액

        // 3. 결제 상태 확인 및 데이터의 가격과 실제 지불된 금액을 비교
        switch (status) {
            case PAID:
                if (!payment.getPrice().equals(finalPrice)) {   // 결제 금액 불일치 시 에러 발생
                    log.error("결제 금액 불일치 ::::: 요청 금액 ::::: {} 최종 결제 금액 ::::: {}", payment.getPrice(), finalPrice);
                    throw new CustomPaymentException(ErrorCode.PAYMENT_PRICE_MISMATCH);
                }
                log.info("결제 완료 ::::: {}", status);
                break;
            case CANCELLED:
            case PARTIAL_CANCELLED:
                throw new CustomPaymentException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
            default:
                throw new CustomPaymentException(ErrorCode.INVALID_PAYMENT_STATUS);
        }

        // 4. 결제 상태 변경
        payment.complete(paymentData.merchantId(), finalPrice, status);

        // TODO: 5. 예약 상태 변경 API 호출

        return PaymentResponse.of(payment);
    }
}
