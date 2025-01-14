package com.haot.reservation.infrastructure.client;

import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.infrastructure.dtos.payment.PaymentCancelRequest;
import com.haot.reservation.infrastructure.dtos.payment.PaymentCreateRequest;
import com.haot.reservation.infrastructure.dtos.payment.PaymentResponse;
import com.haot.submodule.role.Role;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service")
public interface PaymentClient {

  @PostMapping("/api/v1/payments")
  ApiResponse<Map<String, Object>> createPayment(
      @Valid @RequestBody PaymentCreateRequest request,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-User-Role") Role role
  );

  @PostMapping("/api/v1/payments/{paymentId}/cancel")
  ApiResponse<PaymentResponse> cancelPayment(
      @Valid @RequestBody PaymentCancelRequest request,
      @PathVariable String paymentId,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-User-Role") Role role
  );
}
