package com.haot.payment.application.service;

import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import com.haot.payment.domain.model.Payment;
import com.haot.payment.infrastructure.client.PortOneService;
import com.haot.payment.infrastructure.client.ReservationClient;
import com.haot.payment.infrastructure.repository.PaymentRepository;
import com.haot.submodule.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService; // 테스트 대상 서비스

    @Mock
    private PaymentRepository paymentRepository; // 의존성 주입을 Mock 으로 대체

    @Mock
    private PortOneService portOneService;

    @Mock
    private ReservationClient reservationClient;

    private Payment payment;

    @BeforeEach
    void setUp() {
        // 기본 테스트 데이터 초기화
        payment = Payment.create("USER-UUID", "RESERVATION-UUID", 100000.0, PaymentMethod.CARD, PaymentStatus.READY);
    }

    @Test
    @DisplayName("결제 생성 성공 테스트")
    void createPayment() {
        // Given: 테스트 데이터
        PaymentCreateRequest request = new PaymentCreateRequest("USER-UUID", "RESERVATION-UUID", 100000.0, "CARD");

        // Mocking: paymentRepository 동작 설정
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When: 서비스 호출
        PaymentResponse response = paymentService.createPayment(request, "USER-UUID", Role.USER);

        // Then: 결과 검증
        assertNotNull(response);
        assertEquals(payment.getUserId(), response.userId());
        assertEquals(payment.getReservationId(), response.reservationId());
        assertEquals(payment.getPrice(), response.price());
        verify(paymentRepository, times(1)).save(any(Payment.class)); // save 메서드 호출 확인
        System.out.println("결제 생성 성공 시 응답: " + response);
    }

    @Test
    @DisplayName("결제 생성 테스트 - userId 불일치")
    void createPayment_UserIdMismatch() {
        // Given: 테스트 데이터
        PaymentCreateRequest request = new PaymentCreateRequest("USER-UUID-1 ,", "RESERVATION-UUID", 100000.0, "CARD");

        // When: 서비스 호출
        CustomPaymentException exception = assertThrows(CustomPaymentException.class, () -> {
            paymentService.createPayment(request, "USER-UUID", Role.USER);
        });
        // Then: 결과 검증
        assertEquals(ErrorCode.USER_NOT_MATCHED, exception.getResCode());
    }
}