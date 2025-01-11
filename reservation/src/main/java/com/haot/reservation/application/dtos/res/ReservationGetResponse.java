package com.haot.reservation.application.dtos.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haot.reservation.domain.model.Reservation;
import com.haot.reservation.domain.model.ReservationStatus;
import java.time.LocalDate;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ReservationGetResponse(
    String reservationId,
    String userId,
    String lodgeName,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    Integer numGuests,
    String request,
    Double totalPrice,
    ReservationStatus status,
    String paymentId,
    String pointHistoryId,
    String url

) {

  public static ReservationGetResponse of(Reservation reservation, String url) {
    return ReservationGetResponse.builder()
        .reservationId(reservation.getReservationId())
        .userId(reservation.getUserId())
        .lodgeName(reservation.getLodgeName())
        .checkInDate(reservation.getCheckInDate())
        .checkOutDate(reservation.getCheckOutDate())
        .numGuests(reservation.getNumGuests())
        .request(reservation.getRequest())
        .totalPrice(reservation.getTotalPrice())
        .status(reservation.getStatus())
        .paymentId(reservation.getPaymentId())
        .pointHistoryId(reservation.getPointHistoryId())
        .url(url)
        .build();
  }

  public static ReservationGetResponse of(Reservation reservation) {
    return ReservationGetResponse.builder()
        .reservationId(reservation.getReservationId())
        .userId(reservation.getUserId())
        .lodgeName(reservation.getLodgeName())
        .checkInDate(reservation.getCheckInDate())
        .checkOutDate(reservation.getCheckOutDate())
        .numGuests(reservation.getNumGuests())
        .request(reservation.getRequest())
        .totalPrice(reservation.getTotalPrice())
        .status(reservation.getStatus())
        .paymentId(reservation.getPaymentId())
        .pointHistoryId(reservation.getPointHistoryId())
        .build();
  }
}
