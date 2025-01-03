package com.haot.user.application.dto.res;

import lombok.Builder;

@Builder
public record UserCreateResponse(
    String userId
) {
  public static UserCreateResponse of(String userId) {
    return UserCreateResponse.builder()
        .userId(userId)
        .build();
  }
}
