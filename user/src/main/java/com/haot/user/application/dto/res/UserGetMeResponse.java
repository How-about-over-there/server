package com.haot.user.application.dto.res;

import com.haot.user.domain.model.enums.Gender;
import com.haot.user.submodule.role.Role;
import lombok.Builder;

@Builder
public record UserGetMeResponse(
    String name,
    String password,
    String email,
    String phoneNumber,
    String birthDate,
    Gender gender,
    String preferredLanguage,
    String currency,
    String profileImageUrl,
    String address,
    Role role

) {

}
