package com.haot.auth.infrastructure.feign.adapter;

import com.haot.auth.application.dto.req.AuthLoginRequest;
import com.haot.auth.application.dto.req.AuthSignupRequest;
import com.haot.auth.application.feign.port.UserClientPort;
import com.haot.auth.domain.enums.Role;
import com.haot.auth.infrastructure.feign.client.UserClient;
import com.haot.auth.infrastructure.feign.dto.FeignUserCreateRequest;
import com.haot.auth.infrastructure.feign.dto.FeignUserLoginValidationRequest;
import com.haot.auth.infrastructure.feign.dto.FeignUserLoginValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter implements UserClientPort {

  private final UserClient userClient;

  public String createUser(AuthSignupRequest authSignupRequest, Role role) {
    // FeignClient 요청에 맞는 request 로 변환 후 요청
    FeignUserCreateRequest feignUserCreateRequest = FeignUserCreateRequest.builder()
        .name(authSignupRequest.name())
        .password(authSignupRequest.password())
        .email(authSignupRequest.email())
        .phoneNumber(authSignupRequest.phoneNumber())
        .birthDate(authSignupRequest.birthDate())
        .gender(authSignupRequest.gender())
        .preferredLanguage(authSignupRequest.preferredLanguage())
        .currency(authSignupRequest.currency())
        .profileImageUrl(authSignupRequest.profileImageUrl())
        .address(authSignupRequest.address())
        .role(role)
        .build();
    // 응답값 반환
    return userClient.createUser(feignUserCreateRequest).data().userId();
  }

  public FeignUserLoginValidationResponse validationUser(AuthLoginRequest authLoginRequest) {

    FeignUserLoginValidationRequest build = FeignUserLoginValidationRequest.builder()
        .email(authLoginRequest.email())
        .password(authLoginRequest.password())
        .build();

    return userClient.validateLoginInformation(build).data();
  }


}
