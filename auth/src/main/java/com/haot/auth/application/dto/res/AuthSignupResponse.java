package com.haot.auth.application.dto.res;

import com.haot.auth.application.dto.req.AuthSignupRequest;
import lombok.Builder;

@Builder
public record AuthSignupResponse(
    String userId
) {

  public static AuthSignupResponse of(String userId){
    return AuthSignupResponse.builder()
        .userId(userId)
        .build();
  }

}
