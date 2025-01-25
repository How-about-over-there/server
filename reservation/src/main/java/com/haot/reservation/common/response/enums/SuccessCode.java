package com.haot.reservation.common.response.enums;

import com.haot.reservation.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements ResCodeIfs {

  UPDATE_RESERVATION_SUCCESS(HttpStatus.OK, "7002", "예약 상태 변경 성공하였습니다."),
  CANCEL_RESERVATION_SUCCESS(HttpStatus.OK, "7003", "예약 취소에 성공하였습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
