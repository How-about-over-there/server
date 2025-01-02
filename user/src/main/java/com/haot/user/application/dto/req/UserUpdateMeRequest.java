package com.haot.user.application.dto.req;

import lombok.Builder;

@Builder
public record UserUpdateMeRequest(
    String name,
    String password,
    String email,
    String phoneNumber,
    String birthDate,
    String gender,
    String preferredLanguage,
    String currency,
    String profileImageUrl,
    String address
) {

}
