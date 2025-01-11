package com.haot.reservation.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haot.reservation.common.exceptions.FeignClientException;
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
    return new ApiResponse<>("7001", "Success", "API 요청에 성공했습니다", data);
  }

  public static ApiResponse<Void> success() {
    return new ApiResponse<>("7001", "Success", "API 요청에 성공했습니다", null);
  }

  public static ApiResponse<Object> ERROR(ResCodeIfs resCodeIfs) {
    return new ApiResponse<>(resCodeIfs.getCode(), "error", resCodeIfs.getMessage(), null);
  }

  // FeignClientException 실패 응답
  public static <T> ApiResponse<T> ERROR(FeignClientException e) {
    return ApiResponse.<T>builder()
        .statusCode(e.getStatusCode())
        .status(e.getStatus())
        .message(e.getMessage())
        .data(null)
        .build();
  }
}
