package com.haot.reservation.infrastructure.dtos.coupon;

import lombok.Builder;

@Builder
public record FeignConfirmReservationRequest(
    String reservationStatus
) {
  public static FeignConfirmReservationRequest of(String reservationStatus) {
    return FeignConfirmReservationRequest.builder()
        .reservationStatus(reservationStatus)
        .build();
  }
}
