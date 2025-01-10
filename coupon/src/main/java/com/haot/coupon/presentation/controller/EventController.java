package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.request.events.EventSearchRequest;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.coupon.application.service.EventService;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.common.response.enums.SuccessCode;
import com.haot.submodule.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    // TODO 전체 조회할때 일반 user는 삭제된 event는 보여주지 않게!
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{eventId}")
    public ApiResponse<EventSearchResponse> getEvent(@PathVariable(value = "eventId") String eventId) {
        return ApiResponse.SUCCESS(SuccessCode.GET_DETAIL_EVENT_SUCCESS, eventService.getEvent(eventId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<Page<EventSearchResponse>> searchEvent(@RequestHeader("X-User-Role") Role userRole,
                                                              @ModelAttribute EventSearchRequest request,
                                                              Pageable pageable) {
        return ApiResponse.success(eventService.searchEvent(userRole, request, pageable));
    }


}
