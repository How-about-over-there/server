package com.haot.lodge.presentation.request.lodge;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "숙소 검색을 위한 파라미터")
public record LodgeSearchParams(
        String hostId,
        String name,
        String address,
        Integer maxReservationDay,
        Integer maxPersonnel,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
