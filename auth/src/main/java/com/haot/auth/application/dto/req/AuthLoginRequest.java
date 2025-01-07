package com.haot.auth.application.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email,
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    String password
) {

}
