package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.request.events.EventCreateRequest;
import com.haot.coupon.application.dto.request.events.EventModifyRequest;
import com.haot.coupon.application.dto.response.events.EventCreateResponse;
import com.haot.coupon.application.service.AdminEventService;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.common.response.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/events")
public class AdminEventController {

    private final AdminEventService adminEventService;

    // TODO 로그인 된 admin만 가능하게

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<EventCreateResponse> create(@Valid @RequestBody EventCreateRequest eventCreateRequest) {
        return ApiResponse.SUCCESS(SuccessCode.CREATE_EVENT_SUCCESS, adminEventService.create(eventCreateRequest));
    }

    // TODO header로 userId 받아야 한다.
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{eventId}")
    public ApiResponse<Void> modify(@RequestParam(value = "userId") String userId,
                                    @PathVariable(value = "eventId") String eventId,
                                    @Valid @RequestBody EventModifyRequest eventModifyRequest) {

        adminEventService.modify(userId, eventId, eventModifyRequest);
        return ApiResponse.SUCCESS(SuccessCode.MODIFY_EVENT_SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{eventId}")
    public ApiResponse<Void> delete(@PathVariable String eventId) {
        return ApiResponse.success();
    }



}
