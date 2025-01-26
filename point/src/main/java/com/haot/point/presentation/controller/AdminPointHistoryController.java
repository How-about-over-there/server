package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.history.PointHistoryCreateRequest;
import com.haot.point.application.dto.request.history.AdminHistorySearchRequest;
import com.haot.point.application.dto.request.history.PointHistoryUpdateRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.application.service.AdminPointHistoryService;
import com.haot.point.common.response.ApiResponse;
import com.haot.point.presentation.docs.AdminPointHistoryControllerDocs;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/v1/points/histories")
@RequiredArgsConstructor
public class AdminPointHistoryController implements AdminPointHistoryControllerDocs {

    private final AdminPointHistoryService adminPointHistoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RoleCheck(Role.ADMIN)
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

    @PutMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(Role.ADMIN)
    public ApiResponse<Void> updatePointHistory(@Valid @RequestBody PointHistoryUpdateRequest request,
                                                @PathVariable String historyId) {
        return ApiResponse.success();
    }

    @DeleteMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(Role.ADMIN)
    public ApiResponse<Void> deletePointHistory(@PathVariable String historyId) {
        return ApiResponse.success();
    }

    @GetMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(Role.ADMIN)
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(Role.ADMIN)
    public ApiResponse<PageResponse<PointHistoryResponse>> getPointHistories(
            @ModelAttribute AdminHistorySearchRequest request,
            Pageable pageable,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") Role role
    ) {
        return ApiResponse.success(adminPointHistoryService.getPointHistories(request, pageable, userId, role));
    }
}
