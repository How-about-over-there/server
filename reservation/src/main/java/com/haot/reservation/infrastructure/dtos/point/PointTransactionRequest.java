package com.haot.reservation.infrastructure.dtos.point;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PointTransactionRequest(
    @NotNull @Min(0) Double points,
    @NotBlank String type,
    String contextId
) {

  public static PointTransactionRequest of(Double points) {
    return PointTransactionRequest.builder()
        .points(points)
        .type("USE")
        .contextId("포인트 사용")
        .build();
  }
}
