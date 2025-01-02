package com.haot.user.application.service;

import com.haot.user.application.dto.req.UserCreateRequest;
import com.haot.user.application.dto.res.UserCreateResponse;
import com.haot.user.domain.model.User;
import com.haot.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCRUDServiceImpl implements UserCRUDService {

  private final UserRepository userRepository;

  @Transactional
  public UserCreateResponse createUser(UserCreateRequest request) {
    // business logic
    // 1. User 객체 생성
    User user = User.create(
        request.name(),
        request.password(),
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

    // 2. User 객체 저장 (가정: userRepository 사용)
    User savedUser = userRepository.save(user);

    // return : UserCreateResponse 반환
    return UserCreateResponse.builder()
        .userId(savedUser.getId())
        .build();
  }

}
