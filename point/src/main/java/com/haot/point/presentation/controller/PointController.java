package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.PointCreateRequest;
import com.haot.point.application.dto.request.PointTransactionRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.application.service.PointService;
import com.haot.point.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    // 포인트 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PointResponse> createPoint(@Valid @RequestBody PointCreateRequest request) {
        return ApiResponse.success(pointService.createPoint(request));
    }

    // 본인 포인트 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PointResponse> getPointById(@RequestParam(required = false) String userId) {
        return ApiResponse.success(pointService.getPoint(userId));
    }

    // 포인트 적립
    @PostMapping("/{pointId}/earn")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PointAllResponse> earnPoint(@Valid @RequestBody PointTransactionRequest request,
                                                   @PathVariable String pointId) {
        return ApiResponse.success(
                new PointAllResponse(
                        pointId,
                        "HISTORY-UUID",
                        "USER-UUID",
                        request.points(),
                        1000.0 + request.points(),
                        "EARN",
                        request.description(),
                        LocalDateTime.now().plusMonths(3),
                        "ACTIVE"
                )
        );
    }

    // 포인트 사용
    @PostMapping("/{pointId}/use")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PointAllResponse> usePoint(@Valid @RequestBody PointTransactionRequest request,
                                                   @PathVariable String pointId) {
        return ApiResponse.success(
                new PointAllResponse(
                        pointId,
                        "HISTORY-UUID",
                        "USER-UUID",
                        request.points(),
                        1000.0 - request.points(),
                        "USE",
                        request.description(),
                        LocalDateTime.now().plusMonths(3),
                        "ACTIVE"
                )
        );
    }
}
