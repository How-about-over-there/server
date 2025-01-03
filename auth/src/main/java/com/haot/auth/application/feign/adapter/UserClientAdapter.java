package com.haot.auth.application.feign.adapter;

import com.haot.auth.application.dto.req.AuthSignupRequest;
import com.haot.auth.domain.enums.Role;
import com.haot.auth.infrastructure.feign.client.UserClient;
import com.haot.auth.infrastructure.feign.dto.FeignUserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter {

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

}
