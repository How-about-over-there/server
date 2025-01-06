package com.haot.auth.infrastructure.feign.dto;

import com.haot.auth.domain.enums.Role;

public record FeignUserLoginValidationResponse(
    String userId,
    Role role
) {

}
