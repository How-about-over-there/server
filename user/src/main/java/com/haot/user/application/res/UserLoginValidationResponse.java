package com.haot.user.application.res;

import lombok.Builder;

@Builder
public record UserLoginValidationResponse(
    String message,
    Boolean isValid
) {

}
