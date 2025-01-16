package com.haot.payment.application.service;

import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import com.haot.payment.common.response.ApiResponse;
import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import com.haot.payment.domain.model.Payment;
import com.haot.payment.infrastructure.client.PortOneService;
import com.haot.payment.infrastructure.client.ReservationClient;
import com.haot.payment.infrastructure.client.dto.request.ReservationUpdateRequest;
import com.haot.payment.infrastructure.client.dto.response.PortOneResponse;
import com.haot.payment.infrastructure.repository.PaymentRepository;
import com.haot.submodule.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

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

    @Test
    @DisplayName("결제 확인 성공 테스트")
    void completePayment() throws Exception {
        // Given: 테스트 데이터
        PortOneResponse portOneResponse = new PortOneResponse("PAID", "merchant123", new PortOneResponse.Amount(100000));
        // Reflection 으로 id 필드 설정
        Field idField = Payment.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(payment, "PAYMENT-UUID");

        // Mocking: paymentRepository & portOneService 동작 설정
        when(paymentRepository.findByIdAndIsDeletedFalse("PAYMENT-UUID")).thenReturn(Optional.of(payment));
        when(portOneService.getPaymentData("PAYMENT-UUID")).thenReturn(portOneResponse);

        // When: 서비스 호출
        PaymentResponse response = paymentService.completePayment("PAYMENT-UUID", "USER-UUID", Role.USER);

        // Then: 결과 검증
        assertNotNull(response);
        assertEquals(PaymentStatus.PAID, payment.getStatus());
        verify(portOneService, times(1)).getPaymentData("PAYMENT-UUID");
    }

    @Test
    @DisplayName("결제 완료 성공 - 예약 상태 업데이트 성공")
    void completePayment_Success_UpdateReservation() throws Exception {
        // Given: 테스트 데이터
        PortOneResponse portOneResponse = new PortOneResponse("PAID", "merchant123", new PortOneResponse.Amount(100000));
        ReservationUpdateRequest request = new ReservationUpdateRequest("PAYMENT-UUID", "COMPLETED");
        // Reflection 으로 id 필드 설정
        Field idField = Payment.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(payment, "PAYMENT-UUID");

        // Mocking: paymentRepository & portOneService & reservationClient 동작 설정
        when(paymentRepository.findByIdAndIsDeletedFalse("PAYMENT-UUID")).thenReturn(Optional.of(payment));
        when(portOneService.getPaymentData("PAYMENT-UUID")).thenReturn(portOneResponse);
        when(reservationClient.updateReservation(request, "RESERVATION-UUID", "USER-UUID", Role.USER)).thenReturn(ApiResponse.success(null));

        // When: 서비스 호출
        PaymentResponse response = paymentService.completePayment("PAYMENT-UUID", "USER-UUID", Role.USER);

        // Then: 결과 검증
        assertNotNull(response);
        assertEquals(PaymentStatus.PAID.name(), response.status());
        verify(reservationClient, times(1)).updateReservation(
                request, "RESERVATION-UUID", "USER-UUID", Role.USER);
    }
}