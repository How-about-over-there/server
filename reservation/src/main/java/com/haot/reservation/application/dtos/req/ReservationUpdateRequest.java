package com.haot.reservation.application.dtos.req;

import lombok.Getter;

@Getter
public class ReservationUpdateRequest {

  private String paymentId;
  private String status;
}
