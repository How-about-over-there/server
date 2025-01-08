package com.haot.payment.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T> (
        String statusCode,
        String status,
        String message,
        List<String> errorList,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("8000", "SUCCESS", "API 요청에 성공했습니다", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>("8000", "SUCCESS","API 요청에 성공했습니다", null);
    }

    public static ApiResponse<Object> error(ResCodeIfs resCodeIfs){
        return new ApiResponse<>(resCodeIfs.getCode(), "ERROR", resCodeIfs.getMessage(), null, null);
    }

    public static ApiResponse<Object> error(ResCodeIfs resCodeIfs, List<String> errorList) {
        return new ApiResponse<>(resCodeIfs.getCode(), "ERROR", resCodeIfs.getMessage(), errorList, null);
    }

    public ApiResponse(String statusCode, String status, String message, T data){
        this(statusCode, status, message, null, data);
    }

    public ApiResponse(String statusCode, String status, String message, List<String> errorList){
        this(statusCode, status, message, errorList, null);
    }
}