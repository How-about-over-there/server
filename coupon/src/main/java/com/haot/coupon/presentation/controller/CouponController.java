package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.feign.request.FeignVerifyRequest;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponReadMeResponse;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.application.dto.response.coupons.ReservationVerifyResponse;
import com.haot.coupon.application.service.CouponService;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.common.response.enums.SuccessCode;
import com.haot.coupon.domain.model.enums.CouponStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    // TODO Header에서 userId 받아 사용
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public ApiResponse<List<CouponReadMeResponse>> getMyCoupons(String userId) {

        List<CouponReadMeResponse> response = List.of(
                CouponReadMeResponse.builder()
                        .userCouponId("sdlkfsldknfsdaf")
                        .couponName("테스트 쿠폰 1")
                        .couponAvailableDate(LocalDateTime.now().minusDays(1))
                        .couponExpiredDate(LocalDateTime.now().plusDays(1))
                        .couponStatus(CouponStatus.DISTRIBUTED)
                        .build()
                ,
                CouponReadMeResponse.builder()
                        .userCouponId("sdlkfsldknsdfsdffsdaf")
                        .couponName("테스트 쿠폰 2")
                        .couponAvailableDate(LocalDateTime.now().minusDays(1))
                        .couponExpiredDate(LocalDateTime.now().plusDays(1))
                        .couponStatus(CouponStatus.UNUSED)
                        .build()
        );

        return ApiResponse.success(response);
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

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public ApiResponse<ReservationVerifyResponse> verify(@RequestBody FeignVerifyRequest request) {
        return ApiResponse.SUCCESS(SuccessCode.VERIFY_COUPON_SUCCESS, couponService.verify(request));
    }

    // [Feign] 예약 취소 or 확정 API TODO reservationStatus ENUM값 체크 하는 메서드 써서 체크하기
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{reservationCouponId}")
    public ApiResponse<Void> confirmReservation(@PathVariable(value = "reservationCouponId") String reservationCouponId
            , String reservationStatus) {
        return ApiResponse.success();
    }


}
