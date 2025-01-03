package com.haot.auth.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
}