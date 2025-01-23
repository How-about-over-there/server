package com.haot.coupon.presentation.docs;

import com.haot.coupon.application.dto.request.events.EventCreateRequest;
import com.haot.coupon.application.dto.request.events.EventModifyRequest;
import com.haot.coupon.application.dto.response.events.EventCreateResponse;
import com.haot.coupon.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin 이벤트 API Controller", description = "Role이 Admin만 사용할 수 있는 이벤트 API 목록입니다.")
public interface AdminEventControllerDocs {

    @Operation(summary = "이벤트 생성 API", description = "관리자 이벤트 생성 API 입니다.")
    ApiResponse<EventCreateResponse> create(EventCreateRequest eventCreateRequest);

    @Operation(summary = "이벤트 수정 API", description = "관리자 이벤트 수정 API 입니다, 이벤트 강제 종료 가능합니다.")
    ApiResponse<Void> modify(String userId, String eventId, EventModifyRequest eventModifyRequest);

    ApiResponse<Void> delete(String eventId);
}
