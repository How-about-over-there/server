package com.haot.reservation.infrastructure.client;

import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.infrastructure.dtos.payment.PaymentCreateRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {

  @PostMapping("/api/v1/payments")
  ApiResponse<Map<String, Object>> createPayment(@Valid @RequestBody PaymentCreateRequest request);
}
