package com.haot.user.application.dto.req;

import com.haot.user.submodule.role.Role;
import lombok.Builder;

@Builder
public record AdminUserUpdateRequest(
    String name,
    String password,
    String email,
    String phoneNumber,
    String birthDate,
    String gender,
    String preferredLanguage,
    String currency,
    String profileImageUrl,
    String address,
    Role role
) {

}
