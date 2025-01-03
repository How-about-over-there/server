package com.haot.auth.application.service;

import com.haot.auth.application.dto.req.AuthLoginRequest;
import com.haot.auth.application.dto.req.AuthSignupRequest;
import com.haot.auth.application.dto.res.AuthLoginResponse;
import com.haot.auth.application.dto.res.AuthSignupResponse;

public interface AuthService {

  AuthSignupResponse signup(AuthSignupRequest request);
}
