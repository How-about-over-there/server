package com.haot.reservation.infrastructure.client;

import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.infrastructure.dtos.coupon.FeignConfirmReservationRequest;
import com.haot.reservation.infrastructure.dtos.coupon.FeignVerifyRequest;
import com.haot.reservation.infrastructure.dtos.coupon.ReservationVerifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("coupon-service")
public interface CouponClient {

  @PostMapping("/api/v1/coupons/verify")
  ApiResponse<ReservationVerifyResponse> verify(@RequestBody FeignVerifyRequest request);

  // 쿠폰 상태 변경
  @PutMapping("/api/v1/coupons/{reservationCouponId}")
  ApiResponse<Void> confirmReservation(
      @PathVariable(value = "reservationCouponId") String reservationCouponId,
      @RequestBody FeignConfirmReservationRequest request);
}
