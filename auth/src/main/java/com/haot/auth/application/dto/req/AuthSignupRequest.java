package com.haot.auth.application.dto.req;

import com.haot.auth.domain.enums.Gender;
import com.haot.auth.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record AuthSignupRequest(
    @NotBlank(message = "이름은 필수 항목입니다.")
    String name,

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    String password,

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email,

    @NotBlank(message = "전화번호는 필수 항목입니다.")
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
