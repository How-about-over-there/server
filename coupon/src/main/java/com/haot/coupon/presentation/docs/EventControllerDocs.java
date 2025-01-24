package com.haot.coupon.presentation.docs;

import com.haot.coupon.application.dto.request.events.EventSearchRequest;
import com.haot.coupon.application.dto.response.PageResponse;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

@Tag(name = "이벤트 API Controller", description = "쿠폰 API 목록입니다.")
public interface EventControllerDocs {

    @Operation(summary = "이벤트 단건 조회 API", description = "이벤트 단건 조회 API 입니다.")
    ApiResponse<EventSearchResponse> getEvent(String eventId);

    @Operation(summary = "이벤트 검색 API", description = "이벤트 검색 API 입니다.")
    ApiResponse<PageResponse<EventSearchResponse>> searchEvent(Role userRole, EventSearchRequest request, Pageable pageable);
}
