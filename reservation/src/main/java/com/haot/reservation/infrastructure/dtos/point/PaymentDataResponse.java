package com.haot.reservation.infrastructure.dtos.point;

import lombok.Builder;

@Builder
public record PaymentDataResponse(
    String paymentId,
    String paymentUrl
) {

  public static PaymentDataResponse of(String paymentId, String paymentUrl) {
    return PaymentDataResponse.builder()
        .paymentId(paymentId)
        .paymentUrl(paymentUrl)
        .build();
  }
}
