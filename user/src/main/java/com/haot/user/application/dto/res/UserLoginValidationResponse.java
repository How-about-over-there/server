package com.haot.user.application.dto.res;

import com.haot.user.domain.model.enums.Role;
import lombok.Builder;

@Builder
public record UserLoginValidationResponse(
    String userId,
    Role role
) {

  public static UserLoginValidationResponse of(final String userId, final Role role) {
    return UserLoginValidationResponse.builder()
        .userId(userId)
        .role(role)
        .build();
  }

}
