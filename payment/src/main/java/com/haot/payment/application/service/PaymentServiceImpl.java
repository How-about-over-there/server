package com.haot.payment.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haot.payment.application.dto.request.PaymentCancelRequest;
import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.request.PaymentSearchRequest;
import com.haot.payment.application.dto.response.PageResponse;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import com.haot.payment.domain.model.Payment;
import com.haot.payment.infrastructure.client.PortOneService;
import com.haot.payment.infrastructure.client.ReservationClient;
import com.haot.payment.infrastructure.client.dto.request.PortOneCancelRequest;
import com.haot.payment.infrastructure.client.dto.request.ReservationUpdateRequest;
import com.haot.payment.infrastructure.client.dto.response.PortOneCancelResponse;
import com.haot.payment.infrastructure.client.dto.response.PortOneResponse;
import com.haot.payment.infrastructure.repository.PaymentRepository;
import com.haot.submodule.role.Role;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "PaymentServiceImpl")
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final PortOneService portOneService;
    private final ReservationClient reservationClient;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentCreateRequest request, String userId, Role role) {

        // 1. userId 요청 데이터 유효성 검사
        if (role == Role.USER && !request.userId().equals(userId)) {
            throw new CustomPaymentException(ErrorCode.USER_NOT_MATCHED);
        }

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
    public PaymentResponse completePayment(String paymentId, String userId, Role role) {
        // 1. 결제 데이터 확인
        Payment payment = validPayment(paymentId);
        // userId 검증
        validUser(payment, userId, role);

        // 2. 포트원 결제 단건 조회 API 호출
        PortOneResponse paymentData = portOneService.getPaymentData(payment.getId());
        log.info("포트원 결제 정보 ::::: {}", paymentData);

        PaymentStatus status = PaymentStatus.fromString(paymentData.status());   // 결제 상태
        Double finalPrice = paymentData.amount().total().doubleValue(); // 결제 금액

        // 3. 결제 상태 확인 및 데이터의 가격과 실제 지불된 금액을 비교 & 예약 상태 설정
        String reservationStatus = "";
        if (status == PaymentStatus.PAID) {
            if (!payment.getPrice().equals(finalPrice)) {   // 결제 금액 불일치 시 에러 발생
                log.error("결제 금액 불일치 ::::: 요청 금액 ::::: {} 최종 결제 금액 ::::: {}", payment.getPrice(), finalPrice);
                throw new CustomPaymentException(ErrorCode.PAYMENT_PRICE_MISMATCH);
            }
            log.info("결제 완료 ::::: {}", status);
            reservationStatus = "COMPLETED";// 결제 성공 시 예약 상태 COMPLETED 설정
        } else {
            log.info("결제가 취소된 상태 ::::: {}", status);
            reservationStatus = "CANCELED"; // 결제 취소 시 예약 상태 CANCELED 설정
        }

        // 4. 결제 상태 변경
        payment.complete(paymentData.merchantId(), finalPrice, status);

        // 5. 예약 상태 변경 API 호출
        ReservationUpdateRequest reservationUpdateRequest = new ReservationUpdateRequest(payment.getId(), reservationStatus);
        log.info("결제 ID ::::: {}", reservationUpdateRequest.paymentId());
        log.info("상태 ::::: {}", reservationUpdateRequest.status());
        try {
            reservationClient.updateReservation(
                    reservationUpdateRequest,
                    payment.getReservationId(),
                    userId,
                    role);
            log.info("예약 상태 업데이트 완료 ::::: 예약 ID ::::: {} 상태 ::::: {}", payment.getReservationId(), reservationStatus);
        }  catch (FeignException e) {
            log.error("예약 서비스 호출 실패 ::::: 상태 코드: {} 메시지: {}", e.status(), e.contentUTF8());
            throw e; // 핸들러로 예외 전달
        } catch (JsonProcessingException e) {
            throw new CustomPaymentException(ErrorCode.RESERVATION_UNAVAILABLE);
        }
        return PaymentResponse.of(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(String paymentId, String userId, Role role) {
        // 1. 결제 데이터 확인
        Payment payment = validPayment(paymentId);
        // userId 검증
        validUser(payment, userId, role);

        return PaymentResponse.of(payment);
    }

    @Override
    @Transactional
    public PaymentResponse cancelPayment(PaymentCancelRequest request, String paymentId, String userId, Role role) {
        // 1. 결제 데이터 확인
        Payment payment = validPayment(paymentId);
        // userId 검증
        validUser(payment, userId, role);

        // 2. 결제 상태 확인
        PaymentStatus status = payment.getStatus();
        if (!status.name().equals("PAID")) {
            throw new CustomPaymentException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }

        // 환불 정책, 환불 계좌 등이 적용되면 추가 로직 필요
        // 일단 결제 취소에 대한 요청은 결제한 방법으로 결제 전체 금액이 환불되도록 함
        // -> 카드로 100,000원 결제 시, 카드로 100,000원 환불

        // 3. PortOne 결제 취소 요청
        PortOneCancelRequest cancelRequest = PortOneCancelRequest.builder().reason(request.reason()).build();
        PortOneCancelResponse cancelData = portOneService.cancelPayment(cancelRequest, payment.getId());

        // 4. PortOne 응답 상태 확인 및 처리
        String portOneStatus = cancelData.cancellation().status();
        switch (portOneStatus) {
            case "SUCCEEDED":
                payment.updateStatus(PaymentStatus.CANCELLED);
                // 환불 테이블을 따로 둬서 환불 내역 관리하면 좋을 듯
                break;
            case "REQUESTED":
                payment.updateStatus(PaymentStatus.CANCELLED_REQUESTED);
                break;
            default:
                throw new CustomPaymentException(ErrorCode.INVALID_PAYMENT_STATUS);
        }
        return PaymentResponse.of(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> getPayments(PaymentSearchRequest request, Pageable pageable, String userId, Role role) {
        // USER 요청의 경우 userId 헤더 값으로 지정
        if (role == Role.USER) {
            request.setUserId(userId);
        }
        Page<PaymentResponse> payments = paymentRepository.searchPayments(request, pageable);
        return PageResponse.of(payments);
    }

    private Payment validPayment(String paymentId) {
        return paymentRepository.findByIdAndIsDeletedFalse(paymentId)
                .orElseThrow(() -> new CustomPaymentException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    private void validUser(Payment payment, String userId, Role role) {
        if (role == Role.USER && !payment.getUserId().equals(userId)) {
            throw new CustomPaymentException(ErrorCode.USER_NOT_MATCHED);
        }
    }
}
