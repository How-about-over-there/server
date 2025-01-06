package com.haot.user.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haot.user.common.exception.ErrorCode;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    String statusCode,
    String status,
    String message,
    T data
) {

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>("3001", "Success", "API 요청에 성공했습니다", data);
  }

  public static ApiResponse<Void> success() {
    return new ApiResponse<>("3001", "Success", "API 요청에 성공했습니다", null);
  }

  public static ApiResponse<Void> fail(String message) {
    return new ApiResponse<>(ErrorCode.INTERNAL_SERVER_EXCEPTION.getCode(), "ERROR", message, null);
  }

  public static ApiResponse<Void> fail(ErrorCode errorCode) {
    return new ApiResponse<>(errorCode.getCode(), "ERROR", errorCode.getMessage(), null);
  }

  public static ApiResponse<Void> fail(ErrorCode errorCode, String message) {
    return new ApiResponse<>(errorCode.getCode(), "ERROR", message, null);
  }

  public static <T> ApiResponse<T> fail(ErrorCode errorCode, T data) {
    return new ApiResponse<>(errorCode.getCode(), "ERROR", errorCode.getMessage(), data);
  }
}