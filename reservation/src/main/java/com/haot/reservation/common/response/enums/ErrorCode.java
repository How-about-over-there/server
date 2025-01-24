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
  RESERVATION_CHANGE_FAILED(HttpStatus.BAD_REQUEST, "7102", "예약 상태를 변경할 수 없습니다."),
  INVALID_LODGE_RESPONSE_EXCEPTION(HttpStatus.BAD_REQUEST, "7001", "lodge response is invalid"),
  DATE_UNAVAILABLE_EXCEPTION(HttpStatus.BAD_REQUEST, "7002", "예약이 불가능한 날짜입니다."),
  INVALID_CANCELLATION_REASON(HttpStatus.BAD_REQUEST, "7003", "The provided cancellation reason is invalid."),
  PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "7004", "결제 취소 실패"),
  UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST, "7500", "지원하지 않는 정렬 방식입니다."),

  // 7200번 부터는 feignClient 에러입니다.
  FEIGN_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "7200", "Feign 클라이언트 호출 오류 -> "),
  GATEWAY_ERROR(HttpStatus.BAD_GATEWAY, "7201","gateway 오류: "),
  UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "7202", "예기치 않은 오류 -> ");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
