package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.PointStatusRequest;
import com.haot.point.application.dto.response.PointAllResponse;
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
@RequestMapping("/api/v1/points/histories")
public class PointHistoryController {

    // 본인 포인트 내역 조회
    @GetMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PointHistoryResponse> getPointHistoryById(@PathVariable String historyId) {
        return ApiResponse.success(
                new PointHistoryResponse(
                        historyId,
                        "POINT-UUID",
                        "USER-UUID",
                        1000.0,
                        "ERAN",
                        "RESERVATION-UUID EARN POINT",
                        LocalDateTime.now().plusMonths(3),
                        "ACTIVE"
                )
        );
    }

    // 본인 포인트 내역 전체 조회 및 검색
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

    // 포인트 상태 변경
    @PostMapping("/{historyId}/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PointAllResponse> changeStatusPoint(@Valid @RequestBody PointStatusRequest request,
                                                           @PathVariable String historyId) {
        return ApiResponse.success(
                new PointAllResponse(
                        "POINT-UUID",
                        historyId,
                        "USER-UUID",
                        1000.0,
                        1000.0,
                        "USE",
                        "RESERVATION-UUID USE POINT",
                        LocalDateTime.now().plusMonths(3),
                        request.status()
                )
        );
    }

}
