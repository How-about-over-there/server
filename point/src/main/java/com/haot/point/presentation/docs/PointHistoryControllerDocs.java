package com.haot.point.presentation.docs;

import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.request.history.UserPointHistorySearchRequest;
import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

@Tag(name = "포인트 내역 API Controller", description = "포인트 내역 API 목록입니다.")
public interface PointHistoryControllerDocs {

    @Operation(summary = "포인트 내역 단건 조회 API", description = "포인트 내역 단건 조회 API 입니다.")
    ApiResponse<PointHistoryResponse> getPointHistoryById(String historyId, String userId, Role role);

    @Operation(summary = "포인트 내역 전체 조회 및 검색 API", description = "포인트 내역 전체 조회 및 검색 API 입니다.")
    ApiResponse<PageResponse<PointHistoryResponse>> getPointHistories(PointHistorySearchRequest request, Pageable pageable, String userId, Role role);

    @Operation(summary = "포인트 사용/적립/만료 내역 조회 API", description = "포인트 사용/적립/만료 내역 조회 API 입니다.")
    ApiResponse<PageResponse<PointHistoryResponse>> getUserPointHistories(UserPointHistorySearchRequest request, Pageable pageable, String userId, Role role);

    @Operation(summary = "포인트 상태 변경 API", description = "포인트 상태 변경 API 입니다.")
    ApiResponse<PointAllResponse> updateStatusPoint(PointStatusRequest request, String historyId, String userId, Role role);
}
