package com.haot.coupon.application.dto.feign.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰 사용 예약취소 or 확정 REQUEST DTO")
public record FeignConfirmReservationRequest(
        String reservationStatus
) {
}
