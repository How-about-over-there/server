package com.haot.user.application.res;

import lombok.Builder;

@Builder
public record UserValidationResponse(
    Boolean isValid
) {

}
