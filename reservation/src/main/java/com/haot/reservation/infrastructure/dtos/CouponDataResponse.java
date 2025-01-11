package com.haot.reservation.infrastructure.dtos;

import lombok.Builder;

@Builder
public record CouponDataResponse(

    String reservationCouponId,
    Double totalPrice
) {

  public static CouponDataResponse of(String reservationCouponId, Double totalPrice) {
    return CouponDataResponse.builder()
        .reservationCouponId(reservationCouponId)
        .totalPrice(totalPrice)
        .build();
  }
}
