package com.haot.review.common.response.enums;

import com.haot.review.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {

  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "6001", "Review not found."),
  FORBIDDEN_OPERATION(HttpStatus.FORBIDDEN, "6002", "Access denied."),
  UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST, "6500", "지원하지 않는 정렬 방식입니다."),
  FIlE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "6502", "이미지 업로드에 실패했습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
