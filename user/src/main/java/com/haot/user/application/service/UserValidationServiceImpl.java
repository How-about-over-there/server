package com.haot.user.application.service;


import com.haot.user.application.dto.req.UserLoginValidationRequest;
import com.haot.user.application.dto.res.UserLoginValidationResponse;
import com.haot.user.application.dto.res.UserValidationResponse;
import com.haot.user.common.exception.UserException;
import com.haot.user.common.exception.ErrorCode;
import com.haot.user.common.util.Argon2PasswordEncoder;
import com.haot.user.domain.model.User;
import com.haot.user.infrastructure.repository.UserRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

  private final UserRepository userRepository;
  private final StringRedisTemplate stringRedisTemplate;

  @Override
  public UserLoginValidationResponse validateLoginInformation(UserLoginValidationRequest request) {
    // business logic
    // 1. 요청 email 이 DB에 존재하는지 검사
    User findUserByEmail = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND_EXCEPTION));

    // 2. 요청 password 가 일치하는지 검사
    if (!Argon2PasswordEncoder.matches(request.password().toCharArray(), findUserByEmail.getPassword())) {
      throw new UserException(ErrorCode.INVALID_PASSWORD_EXCEPTION);
    }

    ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
    stringStringValueOperations.set("auth:user:valid_id:" + findUserByEmail.getId(),
        findUserByEmail.getId(), 60, TimeUnit.MINUTES);

    // return : 로그인 성공한 유저 정보 반환
    return UserLoginValidationResponse.of(
        findUserByEmail.getId(),
        findUserByEmail.getRole()
    );
  }

  @Override
  public UserValidationResponse validateUserById(String userId) {
    // return : 파라미터로 받은 userId가 User Table 에 존재하는지에 대한 여부 반환
    return UserValidationResponse.of(userRepository.existsById(userId));
  }
}
