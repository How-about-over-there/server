package com.haot.user.application.dto.res;

import lombok.Builder;

@Builder
public record UserCreateResponse(
    String userId
) {

}
