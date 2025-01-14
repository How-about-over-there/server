package com.haot.payment.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haot.payment.common.response.ApiResponse;
import com.haot.payment.infrastructure.client.dto.request.ReservationUpdateRequest;
import com.haot.submodule.role.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "reservation-service")
public interface ReservationClient {

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/v1/reservations/{reservationId}")
    ApiResponse<Void> updateReservation(
            @RequestBody ReservationUpdateRequest reservationUpdateRequest,
            @PathVariable String reservationId,
            @RequestHeader(value = "X-User-Id", required = true) String userId,
            @RequestHeader(value = "X-User-Role", required = true) Role role
    ) throws JsonProcessingException;
}
