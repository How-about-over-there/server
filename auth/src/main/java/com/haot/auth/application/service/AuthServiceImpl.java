package com.haot.auth.application.service;

import com.haot.auth.application.dto.req.AuthLoginRequest;
import com.haot.auth.application.dto.req.AuthSignupRequest;
import com.haot.auth.application.dto.res.AuthLoginResponse;
import com.haot.auth.application.dto.res.AuthSignupResponse;
import com.haot.auth.application.feign.port.UserClientPort;
import com.haot.auth.common.util.JwtUtils;
import com.haot.auth.domain.enums.Role;
import com.haot.auth.infrastructure.feign.dto.FeignUserLoginValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserClientPort userClientPort;
  private final JwtUtils jwtUtils;

  @Override
  public AuthSignupResponse signup(AuthSignupRequest request) {
    // business logic : Role 의 디폴트 값을 USER 로 셋팅
    Role userRoleToSave = (request.role() == null ? Role.USER : request.role());

    // return : user-service 에 유저 저장을 요청한 결과를 반환
    // email 이 중복 될 경우 user-service 에서 예외 처리됨.
    return AuthSignupResponse.of(userClientPort.createUser(request, userRoleToSave));
  }

  @Override
  public AuthLoginResponse login(AuthLoginRequest request) {
    FeignUserLoginValidationResponse feignResponse = userClientPort.validationUser(request);
    return AuthLoginResponse.builder()
        .accessToken(jwtUtils.createToken(feignResponse.userId(), feignResponse.role()))
        .build();
  }

}
