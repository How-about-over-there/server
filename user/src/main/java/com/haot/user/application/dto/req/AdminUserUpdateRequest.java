package com.haot.user.application.dto.req;

import com.haot.submodule.role.Role;
import com.haot.user.domain.model.enums.Gender;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record AdminUserUpdateRequest(
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
    Role role
) {

}
