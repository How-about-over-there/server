package com.haot.payment.presentation.controller;

import com.haot.payment.application.dto.request.PaymentCancelRequest;
import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.request.PaymentSearchRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.application.service.PaymentService;
import com.haot.payment.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j(topic = "PaymentController")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RoleCheck({Role.USER, Role.ADMIN})
    public ApiResponse<Map<String, Object>> createPayment(@Valid @RequestBody PaymentCreateRequest request,
                                                          @RequestHeader("X-User-Id") String userId,
                                                          @RequestHeader("X-User-Role") Role role) {

        PaymentResponse payment = paymentService.createPayment(request, userId, role);
        log.info("paymentId ::::: {}", payment.paymentId());
        // 프론트엔드 URL 반환
        String paymentPageUrl = String.format(
                "/payment.html?paymentId=%s&orderName=%s&amount=%f&payMethod=%s",
                payment.paymentId(),
                payment.reservationId(),
                payment.price(),
                payment.method()
        );
        return ApiResponse.success(Map.of(
                "payment", payment,            // PaymentResponse 객체 포함
                "paymentPageUrl", paymentPageUrl // URL 포함
        ));
    }

    // 결제 확인
    @PostMapping("/{paymentId}/complete")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.USER, Role.ADMIN})
    public ApiResponse<PaymentResponse> completePayment(@PathVariable String paymentId,
                                                        @RequestHeader("X-User-Id") String userId,
                                                        @RequestHeader("X-User-Role") Role role) {
        PaymentResponse payment = paymentService.completePayment(paymentId, userId, role);
        log.info("결제 완료 정보: {}", ApiResponse.success(payment)); // 결제 확인 출력
        return ApiResponse.success(payment);
    }

    // 본인 결제 단건 조회
    @GetMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.USER, Role.ADMIN})
    public ApiResponse<PaymentResponse> getPaymentById(@PathVariable String paymentId) {
        return ApiResponse.success(paymentService.getPaymentById(paymentId));
    }

    // 결제 전체 조회 및 검색
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.USER, Role.ADMIN})
    public ApiResponse<Page<PaymentResponse>> getPayments(
            @ModelAttribute PaymentSearchRequest request,
            @PageableDefault(size = 10, direction = Sort.Direction.ASC, sort = "createdAt") Pageable pageable
    ) {
        return ApiResponse.success(paymentService.getPayments(request, pageable));
    }

    // 결제 취소 요청
    @PostMapping("/{paymentId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.USER, Role.ADMIN})
    public ApiResponse<PaymentResponse> cancelPayment(@Valid @RequestBody PaymentCancelRequest request,
                                                      @PathVariable String paymentId) {
        return ApiResponse.success(paymentService.cancelPayment(request, paymentId));
    }

}
