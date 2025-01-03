package com.haot.auth.application.dto.res;

import lombok.Builder;

@Builder
public record AuthSignupResponse(
    String userId
) {

}
