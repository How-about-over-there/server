package com.haot.payment.presentation.docs;

import com.haot.payment.application.dto.request.PaymentUpdateRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "[Admin] 결제 API Controller", description = "Admin 만 사용할 수 있는 결제 API 목록입니다.")
public interface AdminPaymentControllerDocs {

    @Operation(summary = "결제 생성 API", description = "관리자용 결제 생성 API 입니다.")
    ApiResponse<Void> updatePayment(PaymentUpdateRequest request, String paymentId);

    @Operation(summary = "결제 확인 API", description = "관리자용 결제 확인 API 입니다.")
    ApiResponse<Void> deletePayment(String paymentId);

    @Operation(summary = "결제 단건 조회 API", description = "관리자용 결제 단건 조회 API 입니다.")
    ApiResponse<PaymentResponse> getPaymentById(String paymentId);

    @Operation(summary = "결제 전체 조회 및 검색 API", description = "관리자용 결제 전체 조회 및 검색 API 입니다.")
    ApiResponse<Page<PaymentResponse>> getPayments(Pageable pageable);
}