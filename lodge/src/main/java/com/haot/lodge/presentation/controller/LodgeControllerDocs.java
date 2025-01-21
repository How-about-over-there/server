package com.haot.lodge.presentation.controller;

import com.haot.lodge.application.response.LodgeCreateResponse;
import com.haot.lodge.application.response.LodgeReadAllResponse;
import com.haot.lodge.application.response.LodgeReadOneResponse;

import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.common.response.SliceResponse;
import com.haot.lodge.presentation.request.lodge.LodgeCreateRequest;
import com.haot.lodge.presentation.request.lodge.LodgeSearchParams;
import com.haot.lodge.presentation.request.lodge.LodgeUpdateRequest;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.data.domain.Pageable;


@Tag(name = "숙소 API", description = "숙소 관련 기능에 대한 API 목록입니다.")
public interface LodgeControllerDocs {

    @Operation(summary = "숙소 생성", description = "숙소 생성을 위한 API 입니다.")
    ApiResponse<LodgeCreateResponse> create(
            String userId,
            LodgeCreateRequest request
    );

    @Operation(summary = "숙소 목록 조회", description = "숙소 목록 검색 및 조회를 위한 API 입니다.")
    ApiResponse<SliceResponse<LodgeReadAllResponse>> readAll(
            Pageable pageable,
            LodgeSearchParams searchParams
    );

    @Operation(summary = "숙소 상세 조회", description = "숙소 단 건 상세 조회를 위한 API 입니다.")
    ApiResponse<LodgeReadOneResponse> readOne(
            String lodgeId
    );

    @Operation(summary = "숙소 수정", description = "숙소 기본 정보 수정을 위한 API 입니다.")
    ApiResponse<Void> update(
            String userId,
            Role userRole,
            String lodgeId,
            LodgeUpdateRequest updateRequest
    );

    @Operation(summary = "숙소 삭제", description = "숙소 삭제를 위한 API 입니다.")
    ApiResponse<Void> delete(
            String userId,
            Role userRole,
            String lodgeId
    );

    @Operation(summary = "숙소 유효성 검사", description = "숙소 ID의 유효성을 검사하는 API 입니다.")
    Map<String, Boolean> verify(
            String lodgeId
    );

}
