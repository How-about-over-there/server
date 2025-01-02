package com.haot.user.application.service;


import com.haot.user.application.dto.req.UserLoginValidationRequest;
import com.haot.user.application.dto.res.UserLoginValidationResponse;
import com.haot.user.common.exception.ApplicationException;
import com.haot.user.common.exception.ErrorCode;
import com.haot.user.common.util.Argon2PasswordEncoder;
import com.haot.user.domain.model.User;
import com.haot.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

  private final UserRepository userRepository;

  @Override
  public UserLoginValidationResponse validateLoginInformation(UserLoginValidationRequest request) {
    // business logic
    // 1. 요청 email 이 DB에 존재하는지 검사
    User findUserByEmail = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND_EXCEPTION));

    // 2. 요청 password 가 일치하는지 검사
    if (!Argon2PasswordEncoder.matches(request.password().toCharArray(), findUserByEmail.getPassword())) {
      throw new ApplicationException(ErrorCode.INVALID_PASSWORD_EXCEPTION);
    }

    // return : 로그인 성공한 유저 정보 반환
    return UserLoginValidationResponse.builder()
        .userId(findUserByEmail.getId())
        .role(findUserByEmail.getRole())
        .build();
  }
}
