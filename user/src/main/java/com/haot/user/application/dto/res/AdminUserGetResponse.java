package com.haot.user.application.dto.res;

import com.haot.submodule.role.Role;
import com.haot.user.domain.model.User;
import com.haot.user.domain.model.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AdminUserGetResponse(
    String userId,
    String name,
    String password,
    String email,
    String phoneNumber,
    LocalDate birthDate,
    Gender gender,
    String preferredLanguage,
    String currency,
    String profileImageUrl,
    String address,
    Role role,
    Boolean isDeleted,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy
) {

  public static AdminUserGetResponse of(User user) {
    return AdminUserGetResponse.builder()
        .userId(user.getId())
        .name(user.getName())
        .password(user.getPassword())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .birthDate(user.getBirthDate())
        .gender(user.getGender())
        .preferredLanguage(user.getPreferredLanguage())
        .currency(user.getCurrency())
        .profileImageUrl(user.getProfileImageUrl())
        .address(user.getAddress())
        .role(user.getRole())
        .isDeleted(user.isDeleted())
        .createdAt(user.getCreatedAt())
        .createdBy(user.getCreateBy())
        .updatedAt(user.getUpdatedAt())
        .updatedBy(user.getUpdatedBy())
        .deletedAt(user.getDeleteAt())
        .deletedBy(user.getDeletedBy())
        .build();
  }

}
