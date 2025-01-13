package com.haot.reservation.infrastructure.dtos.coupon;

public record ReservationVerifyResponse(

    String reservationCouponId,
    Double discountedPrice
) {

}
