package com.haot.lodge.presentation.controller;


import com.haot.lodge.application.response.LodgeDateReadResponse;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.common.response.SliceResponse;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateAddRequest;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateSearchParams;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateUpdateRequest;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateUpdateStatusRequest;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;


@Tag(name = "숙소 날짜 API", description = "숙소 날짜 관련 기능에 대한 API 목록입니다.")
public interface LodgeDateControllerDocs {

    @Operation(summary = "숙소 날짜 추가", description = "숙소에 날짜를 추가하기 위한 API 입니다.")
    ApiResponse<Void> add(
            String userId,
            Role userRole,
            LodgeDateAddRequest dateAddRequest
    );

    @Operation(summary = "숙소의 날짜 목록 조회", description = "숙소의 날짜 목록 조회를 위한 API 입니다.")
    ApiResponse<SliceResponse<LodgeDateReadResponse>> readAll(
            Pageable pageable,
            LodgeDateSearchParams searchParams
    );

    @Operation(summary = "숙소 날짜 수정", description = "숙소 날짜의 상세 정보 수정을 위한 API 입니다.")
    ApiResponse<Void> update(
            String userId,
            Role userRole,
            String dateId,
            LodgeDateUpdateRequest request
    );

    @Operation(summary = "숙소 날짜 상태 변경", description = "숙소 날짜의 상태를 변경하기 위한 API 입니다.")
    ApiResponse<Void> updateStatus(
            LodgeDateUpdateStatusRequest request
    );

}
