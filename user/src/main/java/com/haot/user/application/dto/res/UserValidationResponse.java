package com.haot.user.application.dto.res;

import lombok.Builder;

@Builder
public record UserValidationResponse(
    Boolean isValid
) {
  public static UserValidationResponse of(final Boolean isValid) {
    return UserValidationResponse.builder()
        .isValid(isValid)
        .build();
  }
}
