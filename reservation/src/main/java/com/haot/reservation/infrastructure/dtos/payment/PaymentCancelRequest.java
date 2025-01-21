package com.haot.reservation.infrastructure.dtos.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PaymentCancelRequest(
    @NotBlank String reason
    // 일단 본인이 결제한 결제 수단으로만 환불 가능
) {
  public static PaymentCancelRequest of(String reason) {
    return PaymentCancelRequest.builder()
        .reason(reason)
        .build();
  }
}
