package com.haot.user.application.service;

import com.haot.user.application.dto.res.AdminUserGetResponse;
import com.haot.user.common.exception.ErrorCode;
import com.haot.user.common.exception.UserException;
import com.haot.user.domain.model.User;
import com.haot.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserCRUDServiceImpl implements AdminUserCRUDService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public AdminUserGetResponse getUser(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND_EXCEPTION));
    return AdminUserGetResponse.of(user);
  }
}
