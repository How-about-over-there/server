package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.PointTransactionRequest;
import com.haot.point.application.dto.request.point.PointCreateRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.application.service.PointService;
import com.haot.point.common.response.ApiResponse;
import com.haot.point.presentation.docs.PointControllerDocs;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController implements PointControllerDocs {

    private final PointService pointService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RoleCheck({Role.ADMIN, Role.USER})
    public ApiResponse<PointResponse> createPoint(@Valid @RequestBody PointCreateRequest request,
                                                  @RequestHeader("X-User-Id") String userId,
                                                  @RequestHeader("X-User-Role") Role role) {
        return ApiResponse.success(pointService.createPoint(request, userId, role));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.ADMIN, Role.USER})
    public ApiResponse<PointResponse> getPointById(@RequestParam(required = false) String userId,
                                                   @RequestHeader("X-User-Id") String headerUserId,
                                                   @RequestHeader("X-User-Role") Role role) {
        return ApiResponse.success(pointService.getPoint(userId, headerUserId, role));
    }

    @PostMapping("/{pointId}/earn")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.ADMIN, Role.USER})
    public ApiResponse<PointAllResponse> earnPoint(@Valid @RequestBody PointTransactionRequest request,
                                                   @PathVariable String pointId,
                                                   @RequestHeader("X-User-Id") String userId,
                                                   @RequestHeader("X-User-Role") Role role) {
        return ApiResponse.success(pointService.earnPoint(request, pointId, userId, role));
    }

    @PostMapping("/{pointId}/use")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck({Role.ADMIN, Role.USER})
    public ApiResponse<PointAllResponse> usePoint(@Valid @RequestBody PointTransactionRequest request,
                                                  @PathVariable String pointId,
                                                  @RequestHeader("X-User-Id") String userId,
                                                  @RequestHeader("X-User-Role") Role role) {
        return ApiResponse.success(pointService.usePoint(request, pointId, userId, role));
    }
}
