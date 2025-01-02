package com.haot.user.application.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginValidationRequest(
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,}$", message = "비밀번호는 8자 이상, 영어와 숫자로만 이루어져야 합니다.")
    String password
) {

}
