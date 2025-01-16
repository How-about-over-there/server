package com.haot.reservation.infrastructure.dtos.point;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PointStatusRequest(
    @NotBlank String contextId,
    @NotBlank String status,
    String cancelType
) {

  public static PointStatusRequest of(String contextId, String status) {
    return PointStatusRequest.builder()
        .contextId(contextId)
        .status(status)
        .cancelType(null)
        .build();
  }
}
