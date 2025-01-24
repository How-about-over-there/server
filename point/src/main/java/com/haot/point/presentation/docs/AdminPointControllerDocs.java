package com.haot.point.presentation.docs;

import com.haot.point.application.dto.request.point.PointUpdateRequest;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "[Admin] 포인트 API Controller", description = "Admin 만 사용할 수 있는 포인트 API 목록입니다.")
public interface AdminPointControllerDocs {

    @Operation(summary = "포인트 수정 API", description = "관리자용 포인트 수정 API 입니다.")
    ApiResponse<Void> updatePoint(PointUpdateRequest request, String userId);

    @Operation(summary = "포인트 삭제 API", description = "관리자용 포인트 삭제 API 입니다.")
    ApiResponse<Void> deletePoint(String pointId);

    @Operation(summary = "포인트 단건 조회 API", description = "관리자용 포인트 단건 조회 API 입니다.")
    ApiResponse<PointResponse> getPointById(String pointId);

    @Operation(summary = "포인트 전체 조회 및 검색 API", description = "관리자용 포인트 전체 조회 및 검색 API 입니다.")
    ApiResponse<Page<PointResponse>> getPoints(Pageable pageable);

    }
