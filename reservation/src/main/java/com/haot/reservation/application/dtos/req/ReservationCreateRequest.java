package com.haot.reservation.application.dtos.req;

import java.time.LocalDate;

public record ReservationCreateRequest (
    LocalDate checkInDate,
    LocalDate checkOutDate,
    Integer numGuests,
    String request,
    // 숙소 아이디
    String lodgeId,
    // 적용할 포인트
    Double point,
    // 적용할 쿠폰 아이디
    String userCouponId
)
{
}
