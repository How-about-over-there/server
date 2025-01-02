package com.haot.user.application.res;

import com.haot.user.domain.model.enums.Role;
import lombok.Builder;

@Builder
public record UserLoginValidationResponse(
    String userId,
    Role role
) {

}
