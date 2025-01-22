package com.haot.reservation.infrastructure.dtos.lodge;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record LodgeDateUpdateStatusRequest(
    @NotNull List<String> lodgeDateIds,
    @NotNull String status
) {
  public static LodgeDateUpdateStatusRequest of(List<String> lodgeDateIds, String status) {
    return LodgeDateUpdateStatusRequest.builder()
        .lodgeDateIds(lodgeDateIds)
        .status(status)
        .build();
  }
}
