package com.haot.lodge.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haot.lodge.common.exception.ErrorCode;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T> (
        String statusCode,
        String status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>( "5000", "SUCCESS","API 요청에 성공했습니다", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>("5000", "SUCCESS","API 요청에 성공했습니다", null);
    }

    public static ApiResponse<Void> fail(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), "FAIL", errorCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, T data) {
        return new ApiResponse<>(errorCode.getCode(), "ERROR", errorCode.getMessage(), data);
    }
}
