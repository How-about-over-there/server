package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.application.service.PointHistoryService;
import com.haot.point.common.response.ApiResponse;
import com.haot.point.presentation.docs.PointHistoryControllerDocs;
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
public class PointHistoryController implements PointHistoryControllerDocs {

    private final PointHistoryService pointHistoryService;

    @GetMapping("/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.USER, Role.ADMIN})
    public ApiResponse<PointHistoryResponse> getPointHistoryById(@PathVariable String historyId,
                                                                 @RequestHeader("X-User-Id") String userId,
                                                                 @RequestHeader("X-User-Role") Role role) {
        return ApiResponse.success(pointHistoryService.getPointHistoryById(historyId, userId, role));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.USER, Role.ADMIN})
    public ApiResponse<PageResponse<PointHistoryResponse>> getUserPointHistories(
            @ModelAttribute PointHistorySearchRequest request,
            Pageable pageable,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") Role role
    ) {

        return ApiResponse.success(pointHistoryService.getUserPointHistories(request, pageable, userId));
    }

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
