package com.haot.point.presentation.controller;

import com.haot.point.application.dto.request.point.PointUpdateRequest;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.common.response.ApiResponse;
import com.haot.point.presentation.docs.AdminPointControllerDocs;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/points")
public class AdminPointController implements AdminPointControllerDocs {

    @PutMapping("/{pointId}")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(Role.ADMIN)
    public ApiResponse<Void> updatePoint(@Valid @RequestBody PointUpdateRequest request,
                                         @PathVariable String pointId) {
        return ApiResponse.success();
    }

    @DeleteMapping("/{pointId}")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(Role.ADMIN)
    public ApiResponse<Void> deletePoint(@PathVariable String pointId) {
        return ApiResponse.success();
    }

    @GetMapping("/{pointId}")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(Role.ADMIN)
    public ApiResponse<PointResponse> getPointById(@PathVariable String pointId) {
        return ApiResponse.success(
                new PointResponse(
                        pointId,
                        "USER-UUID",
                        1000.0
                )
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(Role.ADMIN)
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
