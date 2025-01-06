package com.haot.auth.common.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {

  // 0000: Common Error
  INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "0000", "예기치 못한 오류가 발생했습니다."),
  NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "0001", "존재하지 않는 리소스입니다."),
  INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "0002", "올바르지 않은 요청 값입니다."),
  UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "0003", "권한이 없는 요청입니다."),
  ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, "0004", "이미 삭제된 리소스입니다."),
  FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "0005", "인가되지 않는 요청입니다."),
  ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "0006", "이미 존재하는 리소스입니다."),
  INVALID_SORT_EXCEPTION(HttpStatus.BAD_REQUEST, "0007", "올바르지 않은 정렬 값입니다."),
  SEARCH_LOG_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "0008", "존재하지 않는 검색 로그입니다."),

  // 3000: User Error
  USER_NOT_FOUND_EXCEPTION(HttpStatus.BAD_REQUEST, "3002", "존재하지 않는 사용자입니다."),
  INVALID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "3003", "비밀번호가 일치하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus httpStatus, String code, String message) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }
}
