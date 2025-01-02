package com.haot.reservation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {

  PENDING_VIRTUAL_ACCOUNT("가상계좌 결제 대기"),
  PENDING("결제 대기"),
  CANCELED("예약 취소"),
  COMPLETED("예약 완료");

  private final String description;
}
