package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.request.EventCreateRequest;
import com.haot.coupon.application.dto.request.EventModifyRequest;
import com.haot.coupon.application.dto.response.EventCreateResponse;
import com.haot.coupon.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/events")
public class AdminEventController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<EventCreateResponse> create(@RequestBody EventCreateRequest eventCreateRequest) {
        return ApiResponse.success(new EventCreateResponse(eventCreateRequest.couponId()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{eventId}")
    public ApiResponse<Void> modify(@PathVariable String eventId, @RequestBody EventModifyRequest eventModifyRequest) {
        return ApiResponse.success();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{eventId}")
    public ApiResponse<Void> delete(@PathVariable String eventId) {
        return ApiResponse.success();
    }



}
