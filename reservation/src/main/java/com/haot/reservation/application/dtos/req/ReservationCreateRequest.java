package com.haot.reservation.application.dtos.req;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ReservationCreateRequest {

  private LocalDate checkInDate;

  private LocalDate checkOutDate;

  private Integer numGuests;

  private String request;
}
