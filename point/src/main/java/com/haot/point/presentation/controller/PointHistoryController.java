package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.request.history.UserPointHistorySearchRequest;
import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.application.service.PointHistoryService;
import com.haot.point.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<PageResponse<PointHistoryResponse>> getPointHistories(
            @ModelAttribute PointHistorySearchRequest request,
            Pageable pageable,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") Role role
    ) {

        return ApiResponse.success(pointHistoryService.getPointHistories(request, pageable, userId, role));
    }

    // 본인 포인트 내역 전체 조회 및 검색 - 사용/적립/만료 내역만
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PageResponse<PointHistoryResponse>> getUserPointHistories(
            @ModelAttribute UserPointHistorySearchRequest request,
            Pageable pageable,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") Role role
    ) {

        return ApiResponse.success(pointHistoryService.getUserPointHistories(request, pageable, userId));
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
