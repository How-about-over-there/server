package com.haot.user.application.dto.req;

import com.haot.user.domain.model.enums.Gender;
import com.haot.user.submodule.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record UserCreateRequest(
    @NotBlank(message = "이름은 필수 항목입니다.")
    String name,

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,}$", message = "비밀번호는 8자 이상, 영어와 숫자로만 이루어져야 합니다.")
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
