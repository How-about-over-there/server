package com.haot.user.application.service;

import com.haot.user.application.dto.req.UserCreateRequest;
import com.haot.user.application.dto.req.UserUpdateMeRequest;
import com.haot.user.application.dto.res.UserCreateResponse;
import com.haot.user.application.dto.res.UserGetMeResponse;
import com.haot.user.common.exception.UserException;
import com.haot.user.common.exception.ErrorCode;
import com.haot.user.common.util.Argon2PasswordEncoder;
import com.haot.user.domain.model.User;
import com.haot.user.infrastructure.repository.UserRepository;
import com.haot.submodule.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCRUDServiceImpl implements UserCRUDService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserCreateResponse createUser(UserCreateRequest request) {
    // validation : 이미 해당 이메일을 가진 유저가 존재하는지 확인
    if (userRepository.existsByEmail(request.email())) {
      throw new UserException((ErrorCode.USER_ALREADY_EXIST_EXCEPTION));
    }

    // business logic
    // 1. request 의 Role 이 ADMIN 일 경우 생성 불가
    if (request.role().equals(Role.ADMIN)) {
      throw new UserException(ErrorCode.UNAUTHORIZED_EXCEPTION);
    }

    // 2. User 객체 생성
    User user = User.create(
        request.name(),
        //Password 는 인코딩 후 저장
        Argon2PasswordEncoder.encode(request.password().toCharArray()),
        request.email(),
        request.phoneNumber(),
        request.role(),
        request.birthDate(),
        request.gender(),
        request.preferredLanguage(),
        request.currency(),
        request.profileImageUrl(),
        request.address()
    );

    // 3. User 객체 저장 (가정: userRepository 사용)
    User savedUser = userRepository.save(user);

    // return : UserCreateResponse 반환
    return UserCreateResponse.of(
        savedUser.getId()
    );
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "userInfoCache", key = "args[0]")
  public UserGetMeResponse getMyInfo(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND_EXCEPTION));
    return UserGetMeResponse.of(user);
  }

  @Override
  @Transactional
  @CacheEvict(cacheNames = "userInfoCache", key = "args[1]")
  public void updateMyInfo(UserUpdateMeRequest userUpdateMeRequest, String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND_EXCEPTION));
    user.updateName(userUpdateMeRequest.name());
    if (userUpdateMeRequest.password() != null) {
      user.updatePassword(Argon2PasswordEncoder.encode(userUpdateMeRequest.password().toCharArray()));
    }
    user.updateEmail(userUpdateMeRequest.email());
    user.updatePhoneNumber(userUpdateMeRequest.phoneNumber());
    user.updateBirthDate(userUpdateMeRequest.birthDate());
    user.updateGender(userUpdateMeRequest.gender());
    user.updatePreferredLanguage(userUpdateMeRequest.preferredLanguage());
    user.updateCurrency(userUpdateMeRequest.currency());
    user.updateProfileImageUrl(userUpdateMeRequest.profileImageUrl());
    user.updateAddress(userUpdateMeRequest.address());
  }
}
