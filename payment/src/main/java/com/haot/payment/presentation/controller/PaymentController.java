package com.haot.payment.presentation.controller;

import com.haot.payment.application.dto.request.PaymentCancelRequest;
import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    // 결제 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponse> createPayment(@Valid @RequestBody PaymentCreateRequest request) {

        return ApiResponse.success(
                new PaymentResponse(
                        "PAYMENT-UUID1",
                        request.userId(),
                        request.reservationId(),
                        "IMP-UID",
                        "MERCHANT-UID",
                        request.price(),
                        100000.0,
                        "CARD",
                        "READY"
                )
        );
    }

    // 결제 단건 조회
    @GetMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PaymentResponse> getPaymentById(@PathVariable String paymentId) {
        return ApiResponse.success(
                new PaymentResponse(
                        paymentId,
                        "USER-UUID",
                        "IMP-UID",
                        "IMP-UID",
                        "MERCHANT-UID",
                        100000.0,
                        100000.0,
                        "CARD",
                        "READY"
                )
        );
    }

    // 결제 전체 조회 및 검색
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<PaymentResponse>> getPayments(Pageable pageable) {

        List<PaymentResponse> list = List.of(
                new PaymentResponse(
                        "PAYMENT-UUID1",
                        "USER-UUID",
                        "IMP-UID",
                        "IMP-UID",
                        "MERCHANT-UID",
                        100000.0,
                        100000.0,
                        "CARD",
                        "READY"
                ),
                new PaymentResponse(
                        "PAYMENT-UUID2",
                        "USER-UUID",
                        "IMP-UID",
                        "IMP-UID",
                        "MERCHANT-UID",
                        100000.0,
                        100000.0,
                        "CARD",
                        "READY"
                )
        );

        return ApiResponse.success(
            new PageImpl<>(list, pageable, list.size())
        );
    }

    // 결제 취소 요청
    @PostMapping("/{paymentId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PaymentResponse> cancelPayment(@Valid @RequestBody PaymentCancelRequest request,
                                                      @PathVariable String paymentId) {
        return ApiResponse.success(
                new PaymentResponse(
                        paymentId,
                        "USER-UUID",
                        "IMP-UID",
                        "IMP-UID",
                        "MERCHANT-UID",
                        request.price(),
                        100000.0,
                        request.method(),
                        "READY"
                )
        );
    }

}
