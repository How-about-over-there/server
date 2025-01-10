package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.feign.request.FeignConfirmReservationRequest;
import com.haot.coupon.application.dto.feign.request.FeignVerifyRequest;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponReadMeResponse;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.application.dto.feign.response.ReservationVerifyResponse;
import com.haot.coupon.application.service.CouponService;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.common.response.enums.SuccessCode;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    @RoleCheck({Role.ADMIN, Role.USER})
    public ApiResponse<Page<CouponReadMeResponse>> getMyCoupons(@RequestHeader("X-User-Id") String userId,
                                                                Pageable pageable) {
        return ApiResponse.SUCCESS(SuccessCode.GET_USER_COUPONS_SUCCESS, couponService.getMyCoupons(userId, pageable));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{couponId}")
    public ApiResponse<CouponSearchResponse> getCouponDetails(@PathVariable(value = "couponId") String couponId) {
        return ApiResponse.SUCCESS(SuccessCode.GET_DETAIL_COUPON_SUCCESS, couponService.getCouponDetails(couponId));
    }

    // TODO userId 받아야 된다.
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/issued")
    public ApiResponse<Void> customerIssueCoupon(@RequestBody CouponCustomerCreateRequest request) {

        String testUserId = UUID.randomUUID().toString();

        couponService.customerIssueCoupon(request, testUserId);

        return ApiResponse.SUCCESS(SuccessCode.CUSTOMER_ISSUED_COUPON_SUCCESS);
    }

    // 유효성 검사 API
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public ApiResponse<ReservationVerifyResponse> verify(@RequestBody FeignVerifyRequest request) {
        return ApiResponse.SUCCESS(SuccessCode.VERIFY_COUPON_SUCCESS, couponService.verify(request));
    }

    // [Feign] 예약 취소 or 확정 API
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{reservationCouponId}")
    public ApiResponse<Void> confirmReservation(@PathVariable(value = "reservationCouponId") String reservationCouponId,
                                                @RequestBody FeignConfirmReservationRequest request) {

        couponService.confirmReservation(reservationCouponId, request);
        return ApiResponse.SUCCESS(SuccessCode.COUPON_RESERVATION_SUCCESS);
    }


}
