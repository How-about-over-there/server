package com.haot.reservation.infrastructure.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReservationStatus {
  UNSELECTABLE("선택 불가능"),
  EMPTY("예약 없음"),
  WAITING("예약 대기"),
  COMPLETE("예약 완료");

  private final String description;
}
