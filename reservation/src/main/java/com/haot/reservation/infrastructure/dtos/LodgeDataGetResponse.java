package com.haot.reservation.infrastructure.dtos;

import java.util.List;
import lombok.Builder;

@Builder
public record LodgeDataGetResponse(
    List<String> lodgeDateIds,
    Double totalPrice
) {

  public static LodgeDataGetResponse of(List<String> lodgeDateIds, Double totalPrice) {
    return LodgeDataGetResponse.builder()
        .lodgeDateIds(lodgeDateIds)
        .totalPrice(totalPrice)
        .build();
  }
}
