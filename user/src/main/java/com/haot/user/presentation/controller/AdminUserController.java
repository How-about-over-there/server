package com.haot.user.presentation.controller;

import com.haot.user.application.req.AdminUserUpdateRequest;
import com.haot.user.application.res.AdminUserGetResponse;
import com.haot.user.common.response.ApiResponse;
import com.haot.user.domain.model.enums.Role;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/users")
public class AdminUserController {
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{userId}")
  public ApiResponse<AdminUserGetResponse> getUserById(
      @PathVariable String userId) {

    AdminUserGetResponse res = new AdminUserGetResponse(
        userId,
        "강찬욱",
        "SecurePassword123",
        "저기어때@naver.com",
        "+821012345678",
        "1990-01-01",
        "male",
        "en",
        "KRW",
        "https://example.com/images/abc.jpg",
        "서울특별시 강남구 테헤란로 123",
        Role.USER,
        false,
        "2024-12-31T12:34:56",
        "550e8400-e29b-41d4-a716-446655440000",
        "2024-12-31T14:56:00",
        "550e8400-e29b-41d4-a716-446655440000",
        null,
        null
    );

    return ApiResponse.success(res);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ApiResponse<Page<AdminUserGetResponse>> getUsers(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String phoneNumber,
      @RequestParam(required = false) String gender,
      @RequestParam(required = false) Boolean isDeleted,
      Pageable pageable) {

    List<AdminUserGetResponse> userList = Arrays.asList(
        new AdminUserGetResponse(
            "550e8400-e29b-41d4-a716-446655440000",
            "강찬욱",
            "SecurePassword123",
            "example@naver.com",
            "+821012345678",
            "1990-01-01",
            "male",
            "en",
            "KRW",
            "https://example.com/images/abc.jpg",
            "서울특별시 강남구 테헤란로 123",
            Role.USER,
            false,
            "2024-12-31T12:34:56",
            "550e8400-e29b-41d4-a716-446655440000",
            "2024-12-31T14:56:00",
            "550e8400-e29b-41d4-a716-446655440000",
            null,
            null
        ),
        new AdminUserGetResponse(
            "550e8400-e29b-41d4-a716-446655440001",
            "김민지",
            "SecurePassword456",
            "minji.kim@example.com",
            "+821055678901",
            "1995-05-15",
            "female",
            "ko",
            "KRW",
            "https://example.com/images/minji.jpg",
            "부산광역시 해운대구 센텀로 45",
            Role.USER,
            false,
            "2024-12-31T12:34:56",
            "550e8400-e29b-41d4-a716-446655440000",
            "2024-12-31T14:56:00",
            "550e8400-e29b-41d4-a716-446655440000",
            null,
            null
        )
    );

    Page<AdminUserGetResponse> res = new PageImpl<>(userList, pageable, userList.size());
    
    return ApiResponse.success(res);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{userId}")
  public ApiResponse<Void> updateUserById(
      @PathVariable String userId,
      @RequestBody(required = false) AdminUserUpdateRequest request) {

    return ApiResponse.success();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{userId}")
  public ApiResponse<Void> deleteUserById(
      @PathVariable String userId) {

    return ApiResponse.success();
  }
}
