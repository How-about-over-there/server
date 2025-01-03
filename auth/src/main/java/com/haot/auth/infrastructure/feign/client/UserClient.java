package com.haot.auth.infrastructure.feign.client;

import com.haot.auth.common.response.ApiResponse;
import com.haot.auth.infrastructure.feign.dto.FeignUserCreateRequest;
import com.haot.auth.infrastructure.feign.dto.FeignUserCreateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {

  @PostMapping("/api/v1/users")
  ApiResponse<FeignUserCreateResponse> createUser(
      @RequestBody FeignUserCreateRequest request);
}
