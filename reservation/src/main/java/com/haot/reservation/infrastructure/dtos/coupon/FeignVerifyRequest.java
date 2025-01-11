package com.haot.reservation.infrastructure.dtos.coupon;

public record FeignVerifyRequest(
    String couponId,
    String userId,
    double reservationPrice
) {
}
