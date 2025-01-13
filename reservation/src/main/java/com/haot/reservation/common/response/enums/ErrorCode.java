package com.haot.reservation.common.response.enums;

import com.haot.reservation.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {

  GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0000", "An unexpected error occurred"),
  RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "7100", "예약 내역을 찾을 수 없습니다"),
  UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "7101", "본인의 예약 내역만 조회할 수 있습니다."),
  INVALID_LODGE_RESPONSE_EXCEPTION(HttpStatus.BAD_REQUEST, "7001", "lodge response is invalid"),
  DATE_UNAVAILABLE_EXCEPTION(HttpStatus.BAD_REQUEST, "7002", "예약이 불가능한 날짜입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
