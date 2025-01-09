package com.haot.point.common.exception.enums;

import com.haot.point.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {

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
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "0009", "입력 데이터 유효성 검증에 실패했습니다."),

    // 9000: Point Error
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "9001", "포인트 정보를 찾을 수 없습니다."),
    POINT_INSUFFICIENT(HttpStatus.BAD_REQUEST, "9002", "포인트 잔액이 부족합니다."),
    POINT_TYPE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "9003", "지원하지 않는 포인트 형식입니다."),
    POINT_STATUS_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "9004", "지원하지 않는 포인트 상태입니다."),
    POINT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "9005", "포인트 내역 정보를 찾을 수 없습니다."),
    POINT_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "9006", "이미 처리된 포인트 입니다." ),
    POINT_CONTEXID_REQUIRED(HttpStatus.BAD_REQUEST, "9007", "요청에 contextId 정보가 필요합니다."),
    PENDING_OPERATION_EXISTS(HttpStatus.BAD_REQUEST, "9008", "현재 처리 중인 포인트가 있어 요청을 수행할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}