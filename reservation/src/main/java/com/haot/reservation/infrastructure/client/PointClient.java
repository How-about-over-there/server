package com.haot.reservation.infrastructure.client;

import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.infrastructure.dtos.point.PointAllResponse;
import com.haot.reservation.infrastructure.dtos.point.PointStatusRequest;
import com.haot.reservation.infrastructure.dtos.point.PointTransactionRequest;
import com.haot.submodule.role.Role;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "point-service")
public interface PointClient {

  // 유저 포인트 사용
  @PostMapping("/api/v1/points/{pointId}/use")
  ApiResponse<PointAllResponse> usePoint(
      @Valid @RequestBody PointTransactionRequest request,
      @PathVariable String pointId,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-User-Role") Role role);

  // 포인트 사용 상태 변경
  @PostMapping("/api/v1/points/histories/{historyId}/status")
  ApiResponse<PointAllResponse> updateStatusPoint(
      @Valid @RequestBody PointStatusRequest request,
      @PathVariable String historyId,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-User-Role") Role role);
}
