package com.haot.reservation.common.response.enums;

import com.haot.reservation.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {

  GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "7777", "예상치 못한 오류가 발생했습니다."),
  RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "7100", "예약 정보를 찾을 수 없습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "7101", "본인의 예약 정보만 확인할 수 있습니다."),
  RESERVATION_CHANGE_FAILED(HttpStatus.BAD_REQUEST, "7102", "예약 상태를 변경할 수 없습니다."),
  INVALID_LODGE_RESPONSE_EXCEPTION(HttpStatus.BAD_REQUEST, "7103", "숙소 응답이 유효하지 않습니다."),
  DATE_UNAVAILABLE_EXCEPTION(HttpStatus.BAD_REQUEST, "7104", "예약할 수 없는 날짜입니다."),
  INVALID_CANCELLATION_REASON(HttpStatus.BAD_REQUEST, "7105", "제공된 취소 사유가 유효하지 않습니다."),
  PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "7106", "결제 취소에 실패했습니다."),
  UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST, "7107", "지원하지 않는 정렬 방식입니다."),

  // 7200번 부터는 feignClient 에러입니다.
  FEIGN_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "7200", "Feign 클라이언트 호출 오류 발생: "),
  GATEWAY_ERROR(HttpStatus.BAD_GATEWAY, "7201","Gateway 오류 발생: "),
  UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "7202", "예기치 못한 오류가 발생: ");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
