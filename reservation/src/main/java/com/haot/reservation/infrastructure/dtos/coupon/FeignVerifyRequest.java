package com.haot.reservation.infrastructure.dtos.coupon;

import lombok.Builder;

@Builder
public record FeignVerifyRequest(
    String couponId,
    String userId,
    double reservationPrice
) {

  public static FeignVerifyRequest of(String couponId, String userId, double reservationPrice) {
    return FeignVerifyRequest.builder()
        .couponId(couponId)
        .userId(userId)
        .reservationPrice(reservationPrice)
        .build();
  }
}
