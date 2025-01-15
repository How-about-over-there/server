package com.haot.user.presentation.controller;

import com.haot.submodule.role.RoleCheck;
import com.haot.user.application.dto.req.AdminUserUpdateRequest;
import com.haot.user.application.dto.res.AdminUserGetResponse;
import com.haot.user.application.service.AdminUserCRUDService;
import com.haot.user.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import com.haot.user.domain.model.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/users")
public class AdminUserController {

  private final AdminUserCRUDService adminUserCRUDService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{userId}")
  @RoleCheck({Role.ADMIN})
  public ApiResponse<AdminUserGetResponse> getUserById(
      @PathVariable String userId) {

    return ApiResponse.success(adminUserCRUDService.getUser(userId));
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
            LocalDate.now(),
            Gender.MALE,
            "en",
            "KRW",
            "https://example.com/images/abc.jpg",
            "서울특별시 강남구 테헤란로 123",
            Role.USER,
            false,
            LocalDateTime.now(),
            "550e8400-e29b-41d4-a716-446655440000",
            LocalDateTime.now(),
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
            LocalDate.now(),
            Gender.FEMALE,
            "ko",
            "KRW",
            "https://example.com/images/minji.jpg",
            "부산광역시 해운대구 센텀로 45",
            Role.USER,
            false,
            LocalDateTime.now(),
            "550e8400-e29b-41d4-a716-446655440000",
            LocalDateTime.now(),
            "550e8400-e29b-41d4-a716-446655440000",
            null,
            null
        )
    );

    Page<AdminUserGetResponse> res = new PageImpl<>(userList, pageable, userList.size());
    
    return ApiResponse.success(res);
  }

  /*
  TODO: API 개발 후 @RequestBody(required = false) 에서 required 프로퍼티 제거 및 @Valid 추가하기
 */
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
