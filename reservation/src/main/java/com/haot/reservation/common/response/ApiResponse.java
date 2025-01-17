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

  public static <T> ApiResponse<T> SUCCESS(T data) {
    return new ApiResponse<>("7001", "SUCCESS", "API 요청에 성공했습니다", data);
  }

  public static ApiResponse<Void> SUCCESS(ResCodeIfs resCodeIfs) {
    return new ApiResponse<>(resCodeIfs.getCode(), "SUCCESS", resCodeIfs.getMessage(),null);
  }

  public static ApiResponse<Object> ERROR(ResCodeIfs resCodeIfs) {
    return new ApiResponse<>(resCodeIfs.getCode(), "ERROR", resCodeIfs.getMessage(), null);
  }

  public static <T> ApiResponse<T> ERROR(FeignClientException e) {
    return ApiResponse.<T>builder()
        .statusCode(e.getStatusCode())
        .status(e.getStatus())
        .message(e.getMessage())
        .data(null)
        .build();
  }
}
