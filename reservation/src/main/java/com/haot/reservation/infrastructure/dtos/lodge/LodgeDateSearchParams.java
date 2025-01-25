package com.haot.reservation.infrastructure.dtos.lodge;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record LodgeDateSearchParams(
    @NotBlank String lodgeId,
    LocalDate start,
    LocalDate end
) {

  public static LodgeDateSearchParams of(String lodgeId, LocalDate start, LocalDate end) {
    return LodgeDateSearchParams.builder()
        .lodgeId(lodgeId)
        .start(start)
        .end(end)
        .build();
  }
}
