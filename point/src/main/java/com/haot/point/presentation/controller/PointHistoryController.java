package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.application.service.PointHistoryService;
import com.haot.point.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/points/histories")
@RequiredArgsConstructor
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    // 본인 포인트 내역 조회
    @GetMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PointHistoryResponse> getPointHistoryById(@PathVariable String historyId,
                                                                 @RequestHeader("X-User-Id") String userId,
                                                                 @RequestHeader("X-User-Role") Role role) {
        return ApiResponse.success(pointHistoryService.getPointHistoryById(historyId, userId, role));
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
    @RoleCheck({Role.ADMIN, Role.USER})
    public ApiResponse<PointAllResponse> updateStatusPoint(@Valid @RequestBody PointStatusRequest request,
                                                           @PathVariable String historyId,
                                                           @RequestHeader("X-User-Id") String userId,
                                                           @RequestHeader("X-User-Role") Role role) {
        return ApiResponse.success(pointHistoryService.updateStatusPoint(request, historyId, userId, role));
    }

}
