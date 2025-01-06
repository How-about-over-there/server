package com.haot.auth.infrastructure.feign.dto;

import com.haot.auth.domain.enums.Gender;
import com.haot.auth.domain.enums.Role;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeignUserCreateRequest(
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
