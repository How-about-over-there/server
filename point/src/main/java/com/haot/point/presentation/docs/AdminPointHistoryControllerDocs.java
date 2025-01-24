package com.haot.point.presentation.docs;

import com.haot.point.application.dto.request.history.PointHistoryCreateRequest;
import com.haot.point.application.dto.request.history.PointHistoryUpdateRequest;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "[Admin] 포인트 내역 API Controller", description = "Admin 만 사용할 수 있는 포인트 내역 API 목록입니다.")
public interface AdminPointHistoryControllerDocs {

    @Operation(summary = "포인트 내역 생성 API", description = "관리자용 포인트 내역 생성 API 입니다.")
    ApiResponse<PointHistoryResponse> createPointHistory(PointHistoryCreateRequest request);

    @Operation(summary = "포인트 내역 수정 API", description = "관리자용 포인트 내역 수정 API 입니다.")
    ApiResponse<Void> updatePointHistory(PointHistoryUpdateRequest request, String historyId);

    @Operation(summary = "포인트 내역 삭제 API", description = "관리자용 포인트 내역 삭제 API 입니다.")
    ApiResponse<Void> deletePointHistory(String historyId);

    @Operation(summary = "포인트 내역 단건 조회 API", description = "관리자용 포인트 내역 단건 조회 API 입니다.")
    ApiResponse<PointHistoryResponse> getPointHistoryById(String historyId);

    @Operation(summary = "포인트 내역 전체 조회 및 검색 API", description = "관리자용 포인트 내역 전체 조회 및 검색 API 입니다.")
    ApiResponse<Page<PointHistoryResponse>> getPointHistories(Pageable pageable);

}
