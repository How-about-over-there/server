package com.haot.user.application.dto.res;

import com.haot.user.domain.model.User;
import com.haot.user.domain.model.enums.Gender;
import com.haot.submodule.role.Role;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserGetMeResponse(
    String name,
    String email,
    String phoneNumber,
    LocalDate birthDate,
    Gender gender,
    String preferredLanguage,
    String currency,
    String profileImageUrl,
    String address,
    Role role

) {

  public static UserGetMeResponse of(User user) {
    return UserGetMeResponse.builder()
        .name(user.getName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .birthDate(user.getBirthDate())
        .gender(user.getGender())
        .preferredLanguage(user.getPreferredLanguage())
        .currency(user.getCurrency())
        .profileImageUrl(user.getProfileImageUrl())
        .address(user.getAddress())
        .role(user.getRole())
        .build();
  }

}
