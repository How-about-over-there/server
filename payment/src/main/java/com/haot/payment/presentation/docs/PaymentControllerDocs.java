package com.haot.payment.presentation.docs;

import com.haot.payment.application.dto.request.PaymentCancelRequest;
import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.request.PaymentSearchRequest;
import com.haot.payment.application.dto.response.PageResponse;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Tag(name = "결제 API Controller", description = "결제 API 목록입니다.")
public interface PaymentControllerDocs {

    @Operation(summary = "결제 생성 API", description = "결제 생성 API 입니다.")
    ApiResponse<Map<String, Object>> createPayment(PaymentCreateRequest request, String userId, Role role, String token);

    @Operation(summary = "결제 확인 API", description = "결제 확인 API 입니다.")
    ApiResponse<PaymentResponse> completePayment(String paymentId, String userId, Role role);

    @Operation(summary = "결제 단건 조회 API", description = "결제 단건 조회 API 입니다.")
    ApiResponse<PaymentResponse> getPaymentById(String paymentId, String userId, Role role);

    @Operation(summary = "결제 전체 조회 및 검색 API", description = "결제 전체 조회 및 검색 API 입니다.")
    ApiResponse<PageResponse<PaymentResponse>> getPayments(PaymentSearchRequest request, Pageable pageable, String userId, Role role);

    @Operation(summary = "결제 취소 API", description = "결제 취소 API 입니다.")
    ApiResponse<PaymentResponse> cancelPayment(PaymentCancelRequest request, String paymentId, String userId, Role role);

}
