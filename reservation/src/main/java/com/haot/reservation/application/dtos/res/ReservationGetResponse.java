package com.haot.reservation.application.dtos.res;

import com.haot.reservation.domain.model.ReservationStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationGetResponse {

  private String reservationId;

  private String userId;

  private String lodgeName;

  private LocalDate checkInDate;

  private LocalDate checkOutDate;

  private Integer numGuests;

  private String request;

  private Integer totalPrice;

  private ReservationStatus status;

  private String paymentId;
}
