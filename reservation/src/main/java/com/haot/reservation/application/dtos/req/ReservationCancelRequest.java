package com.haot.reservation.application.dtos.req;

import jakarta.validation.constraints.NotBlank;

public record ReservationCancelRequest(
    @NotBlank
    String reason
) {

}
