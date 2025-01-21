package com.haot.coupon.application.dto.request.events;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "관리자 이벤트 생성 REQUEST DTO")
public record EventCreateRequest(

        @NotBlank
        String couponId,

        @FutureOrPresent
        @NotNull
        LocalDateTime eventStartDate,

        @FutureOrPresent
        @NotNull
        LocalDateTime eventEndDate,

        @NotBlank
        String eventName,

        @NotBlank
        String eventDescription
) {
}
