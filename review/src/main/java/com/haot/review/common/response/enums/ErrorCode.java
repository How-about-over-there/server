package com.haot.review.common.response.enums;

import com.haot.review.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {

  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "6101", "리뷰를 찾을 수 없습니다."),
  FORBIDDEN_OPERATION(HttpStatus.FORBIDDEN, "6102", "접근이 거부되었습니다."),
  UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST, "6103", "지원하지 않는 정렬 방식입니다."),
  FIlE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "6104", "이미지 업로드에 실패했습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
