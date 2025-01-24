package com.haot.reservation.infrastructure.dtos.coupon;

import lombok.Builder;

@Builder
public record FeignVerifyRequest(
    String couponId,
    double reservationPrice
) {

  public static FeignVerifyRequest of(String couponId, double reservationPrice) {
    return FeignVerifyRequest.builder()
        .couponId(couponId)
        .reservationPrice(reservationPrice)
        .build();
  }
}
