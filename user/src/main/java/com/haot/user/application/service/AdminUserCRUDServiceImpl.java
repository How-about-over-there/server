package com.haot.user.application.service;

import com.haot.user.application.dto.req.AdminUserUpdateRequest;
import com.haot.user.application.dto.res.AdminUserGetResponse;
import com.haot.user.common.exception.ErrorCode;
import com.haot.user.common.exception.UserException;
import com.haot.user.common.util.Argon2PasswordEncoder;
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

  @Override
  @Transactional
  public void updateUser(String userId, AdminUserUpdateRequest adminUserUpdateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND_EXCEPTION));
    user.updateName(adminUserUpdateRequest.name());
    if (adminUserUpdateRequest.password() != null) {
      user.updatePassword(Argon2PasswordEncoder.encode(adminUserUpdateRequest.password().toCharArray()));
    }
    user.updateEmail(adminUserUpdateRequest.email());
    user.updatePhoneNumber(adminUserUpdateRequest.phoneNumber());
    user.updateBirthDate(adminUserUpdateRequest.birthDate());
    user.updateGender(adminUserUpdateRequest.gender());
    user.updatePreferredLanguage(adminUserUpdateRequest.preferredLanguage());
    user.updateCurrency(adminUserUpdateRequest.currency());
    user.updateProfileImageUrl(adminUserUpdateRequest.profileImageUrl());
    user.updateAddress(adminUserUpdateRequest.address());
    user.updateRole(adminUserUpdateRequest.role());
  }
}
