package com.haot.reservation.infrastructure.dtos.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PaymentCreateRequest(
    String userId,
    @NotBlank String reservationId,
    @NotNull @Min(0) Double price,
    @NotNull String method
) {
  public static PaymentCreateRequest of(String userId, String reservationId, Double price, String method) {
    return PaymentCreateRequest.builder()
        .userId(userId)
        .reservationId(reservationId)
        .price(price)
        .method(method)
        .build();
  }
}
