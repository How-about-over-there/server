package com.haot.user.application.dto.res;

import com.haot.user.submodule.role.Role;

public record AdminUserGetResponse(
    String userId,
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
    Role role,
    Boolean isDeleted,
    String createdAt,
    String createdBy,
    String updatedAt,
    String updatedBy,
    String deletedAt,
    String deletedBy
) {

}
