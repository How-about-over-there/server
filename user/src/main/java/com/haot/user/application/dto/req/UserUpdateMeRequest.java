package com.haot.user.application.dto.req;

import com.haot.user.domain.model.enums.Gender;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserUpdateMeRequest(
    String name,
    String password,
    String email,
    String phoneNumber,
    LocalDate birthDate,
    Gender gender,
    String preferredLanguage,
    String currency,
    String profileImageUrl,
    String address
) {

}
