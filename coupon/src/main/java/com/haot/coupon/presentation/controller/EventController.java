package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.request.events.EventSearchRequest;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.coupon.application.service.EventService;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.common.response.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public ApiResponse<Page<EventSearchResponse>> searchEvent(@ModelAttribute EventSearchRequest request,
                                                              Pageable pageable) {

        List<EventSearchResponse> response = List.of(
                EventSearchResponse.builder()
                        .eventId("afdknfjkdsbfds")
                        .couponId("skfjalsdkfsalkdnf")
                        .eventStartDate(LocalDateTime.now())
                        .eventEndDate(LocalDateTime.now().plusWeeks(1))
                        .eventName("테스트 이벤트")
                        .eventDescription("연초 숙박 이벤트 ~")
                        .build()
                ,
                EventSearchResponse.builder()
                        .eventId("sdfahadslfnslkdnf")
                        .couponId("sdfdfdfdfasdf")
                        .eventStartDate(LocalDateTime.now())
                        .eventEndDate(LocalDateTime.now().plusWeeks(2))
                        .eventName("테스트 이벤트2")
                        .eventDescription("연초 숙박 이벤트 22")
                        .build()
        );

        return ApiResponse.success(new PageImpl<>(response, pageable, response.size()));
    }




}
