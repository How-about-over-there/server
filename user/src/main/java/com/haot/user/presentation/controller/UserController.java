package com.haot.user.presentation.controller;

import com.haot.user.application.dto.req.UserLoginValidationRequest;
import com.haot.user.application.dto.req.UserUpdateMeRequest;
import com.haot.user.application.dto.res.UserGetMeResponse;
import com.haot.user.application.dto.res.UserLoginValidationResponse;
import com.haot.user.application.dto.res.UserValidationResponse;
import com.haot.user.application.service.UserValidationService;
import com.haot.user.common.response.ApiResponse;
import com.haot.user.domain.model.enums.Gender;
import com.haot.user.domain.model.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserValidationService userValidationService;

  /*
  TODO: API 개발 후 @RequestBody(required = false) 에서 required 프로퍼티 제거 및 @Valid 추가하기
 */
  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/validation")
  public ApiResponse<UserLoginValidationResponse> validateLoginInformation(
      @Valid @RequestBody UserLoginValidationRequest request) {

    return ApiResponse.success(userValidationService.validateLoginInformation(request));
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/{userId}/validation")
  public ApiResponse<UserValidationResponse> validateUser(
      @PathVariable("userId") String userIdToValidate) {

    UserValidationResponse res = UserValidationResponse.builder()
        .isValid(true)
        .build();

    return ApiResponse.success(res);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/me")
  public ApiResponse<UserGetMeResponse> getMyInfo() {

    UserGetMeResponse res = UserGetMeResponse.builder()
        .name("강찬욱")
        .password("SecurePassword123")
        .email("저기어때@naver.com")
        .phoneNumber("+821012345678")
        .birthDate("1990-01-01")
        .gender(Gender.MALE)
        .preferredLanguage("en")
        .currency("KRW")
        .profileImageUrl("https://example.com/images/abc.jpg")
        .address("서울특별시 강남구 테헤란로 123")
        .role(Role.USER)
        .build();

    return ApiResponse.success(res);
  }

  /*
  TODO: API 개발 후 @RequestBody(required = false) 에서 required 프로퍼티 제거 및 @Valid 추가하기
 */
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/me")
  public ApiResponse<Void> updateMyInfo(
      @RequestBody(required = false) UserUpdateMeRequest request) {

    return ApiResponse.success();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/me")
  public ApiResponse<Void> deleteMyInfo() {

    return ApiResponse.success();
  }

}
