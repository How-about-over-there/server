package com.haot.user.application.req;

import jakarta.validation.constraints.NotNull;

public record UserLoginValidationRequest(
    @NotNull
    String email,
    @NotNull
    String password
) {

}
