package com.haot.auth.application.feign.port;

import com.haot.auth.application.dto.req.AuthLoginRequest;
import com.haot.auth.application.dto.req.AuthSignupRequest;
import com.haot.auth.domain.enums.Role;
import com.haot.auth.infrastructure.feign.dto.FeignUserLoginValidationResponse;

public interface UserClientPort {

  String createUser(AuthSignupRequest authSignupRequest, Role role);

  FeignUserLoginValidationResponse validationUser(AuthLoginRequest authLoginRequest);
}
