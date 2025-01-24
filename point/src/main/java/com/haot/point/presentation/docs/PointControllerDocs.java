package com.haot.point.presentation.docs;

import com.haot.point.application.dto.request.PointTransactionRequest;
import com.haot.point.application.dto.request.point.PointCreateRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "포인트 API Controller", description = "포인트 API 목록입니다.")
public interface PointControllerDocs {

    @Operation(summary = "포인트 생성 API", description = "포인트 생성 API 입니다.")
    ApiResponse<PointResponse> createPoint(PointCreateRequest request, String userId, Role role);

    @Operation(summary = "포인트 단건 조회 API", description = "포인트 단건 조회 API 입니다.")
    ApiResponse<PointResponse> getPointById(String userId, String headerUserId, Role role);

    @Operation(summary = "포인트 적립 API", description = "포인트 적립 API 입니다.")
    ApiResponse<PointAllResponse> earnPoint(PointTransactionRequest request, String pointId, String userId, Role role);

    @Operation(summary = "포인트 사용 API", description = "포인트 사용 API 입니다.")
    ApiResponse<PointAllResponse> usePoint(PointTransactionRequest request, String pointId, String userId, Role role);
}
