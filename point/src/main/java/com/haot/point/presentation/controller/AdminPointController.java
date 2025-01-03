package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.PointHistoryUpdateRequest;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/points")
public class AdminPointController {

    // 포인트 수정
    @PutMapping("/{pointId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> updatePoint(@Valid @RequestBody PointHistoryUpdateRequest request,
                                         @PathVariable String pointId) {
        return ApiResponse.success();
    }

    // 포인트 삭제
    @DeleteMapping("/{pointId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deletePoint(@PathVariable String pointId) {
        return ApiResponse.success();
    }

    // 포인트 단건 조회
    @GetMapping("/{pointId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PointResponse> getPointById(@PathVariable String pointId) {
        return ApiResponse.success(
                new PointResponse(
                        pointId,
                        "USER-UUID",
                        1000.0
                )
        );
    }

    // 포인트 전체 조회 및 검색
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<PointResponse>> getPoints(Pageable pageable) {

        List<PointResponse> list = List.of(
                new PointResponse(
                        "POINT-UUID1",
                        "USER-UUID1",
                        1000.0
                ),
                new PointResponse(
                        "POINT-UUID2",
                        "USER-UUID2",
                        1000.0
                )
        );

        return ApiResponse.success(
                new PageImpl<>(list, pageable, list.size())
        );
    }
}
