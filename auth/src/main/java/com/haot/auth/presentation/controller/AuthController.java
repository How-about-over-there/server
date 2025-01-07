package com.haot.auth.presentation.controller;

import com.haot.auth.application.dto.req.AuthLoginRequest;
import com.haot.auth.application.dto.req.AuthSignupRequest;
import com.haot.auth.application.dto.res.AuthLoginResponse;
import com.haot.auth.application.dto.res.AuthSignupResponse;
import com.haot.auth.application.service.AuthService;
import com.haot.auth.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ApiResponse<AuthSignupResponse> signup(@Valid @RequestBody AuthSignupRequest request){
    return ApiResponse.success(authService.signup(request));
  }

  @PostMapping("/login")
  public ApiResponse<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request){
    return ApiResponse.success(authService.login(request));
  }

}
