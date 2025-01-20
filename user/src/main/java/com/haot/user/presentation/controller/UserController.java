package com.haot.user.presentation.controller;

import com.haot.user.application.dto.req.UserCreateRequest;
import com.haot.user.application.dto.req.UserLoginValidationRequest;
import com.haot.user.application.dto.req.UserUpdateMeRequest;
import com.haot.user.application.dto.res.UserCreateResponse;
import com.haot.user.application.dto.res.UserGetMeResponse;
import com.haot.user.application.dto.res.UserLoginValidationResponse;
import com.haot.user.application.dto.res.UserValidationResponse;
import com.haot.user.application.service.UserCRUDService;
import com.haot.user.application.service.UserValidationService;
import com.haot.user.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserValidationService userValidationService;
  private final UserCRUDService userCRUDService;

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

    return ApiResponse.success(userValidationService.validateUserById(userIdToValidate));
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping
  public ApiResponse<UserCreateResponse> createUser(
      @Valid @RequestBody UserCreateRequest request) {

    return ApiResponse.success(userCRUDService.createUser(request));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/me")
  public ApiResponse<UserGetMeResponse> getMyInfo(@RequestHeader("X-User-Id") String userId) {
    return ApiResponse.success(userCRUDService.getMyInfo(userId));
  }


  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/me")
  public ApiResponse<Void> updateMyInfo(
      @Valid @RequestBody UserUpdateMeRequest request,
      @RequestHeader("X-User-Id") String userId) {
    userCRUDService.updateMyInfo(request, userId);
    return ApiResponse.success();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/me")
  public ApiResponse<Void> deleteMyInfo() {

    return ApiResponse.success();
  }

}
