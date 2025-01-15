package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.history.PointHistoryCreateRequest;
import com.haot.point.application.dto.request.history.PointHistoryUpdateRequest;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/v1/points/histories")
public class AdminPointHistoryController {

    // 포인트 내역 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PointHistoryResponse> createPointHistory(@Valid @RequestBody PointHistoryCreateRequest request) {
        return ApiResponse.success(
                new PointHistoryResponse(
                        "HISTORY-UUID",
                        request.pointId(),
                        "USER-UUID",
                        request.points(),
                        request.type(),
                        request.description(),
                        LocalDateTime.now().plusMonths(3),
                        "ACTIVE"
                )
        );
    }

    // 포인트 내역 수정
    @PutMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> updatePointHistory(@Valid @RequestBody PointHistoryUpdateRequest request,
                                                @PathVariable String historyId) {
        return ApiResponse.success();
    }

    // 포인트 내역 삭제
    @DeleteMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deletePointHistory(@PathVariable String historyId) {
        return ApiResponse.success();
    }

    // 포인트 내역 단건 조회
    @GetMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PointHistoryResponse> getPointHistoryById(@PathVariable String historyId) {
        return ApiResponse.success(
                new PointHistoryResponse(
                        historyId,
                        "POINT-UUID",
                        "USER-UUID",
                        1000.0,
                        "EARN",
                        "RESERVATION-UUID USE POINT",
                        LocalDateTime.now().plusMonths(3),
                        "ACTIVE"
                )
        );
    }

    // 포인트 내역 전체 조회 및 검색
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<PointHistoryResponse>> getPointHistories(Pageable pageable) {

        List<PointHistoryResponse> list = List.of(
                new PointHistoryResponse(
                        "HISTORY-UUID1",
                        "POINT-UUID",
                        "USER-UUID",
                        1000.0,
                        "ERAN",
                        "RESERVATION-UUID EARN POINT",
                        LocalDateTime.now().plusMonths(3),
                        "ACTIVE"
                ),
                new PointHistoryResponse(
                        "HISTORY-UUID2",
                        "POINT-UUID",
                        "USER-UUID",
                        1000.0,
                        "USE",
                        "RESERVATION-UUID USE POINT",
                        LocalDateTime.now().plusMonths(3),
                        "ACTIVE"
                )
        );

        return ApiResponse.success(
                new PageImpl<>(list, pageable, list.size())
        );
    }
}
