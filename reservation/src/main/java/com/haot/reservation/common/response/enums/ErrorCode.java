package com.haot.reservation.common.response.enums;

import com.haot.reservation.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {


  INVALID_LODGE_RESPONSE_EXCEPTION(HttpStatus.BAD_REQUEST, "7001", "lodge response is invalid"),
  DATE_UNAVAILABLE_EXCEPTION(HttpStatus.BAD_REQUEST, "7002", "예약이 불가능한 날짜입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
