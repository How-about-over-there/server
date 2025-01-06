package com.haot.auth.application.feign.port;

import com.haot.auth.application.dto.req.AuthSignupRequest;
import com.haot.auth.domain.enums.Role;

public interface UserClientPort {

  String createUser(AuthSignupRequest authSignupRequest, Role role);
}
