package com.haot.coupon.application.dto.request.events;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

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
