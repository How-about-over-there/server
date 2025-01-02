package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponReadMeResponse;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.application.dto.response.coupons.ReservationVerifyResponse;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.domain.model.enums.CouponStatus;
import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

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
    public ApiResponse<CouponSearchResponse> getCouponDetails(@PathVariable String couponId) {
        return ApiResponse.success(CouponSearchResponse.builder()
                .couponId(couponId)
                .couponName("테스트 쿠폰 1")
                .couponAvailableDate(LocalDateTime.now().minusDays(1))
                .couponExpiredDate(LocalDateTime.now().plusDays(1))
                .couponType(CouponType.PRIORITY)
                .discountPolicy(DiscountPolicy.PERCENTAGE)
                .maximumAmount(500000)
                .minimumAmount(50000)
                .discountRate(10)
                .maxQuantity(2000)
                .issuedQuantity(300)
                .build());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/issued")
    public ApiResponse<Void> issueCoupon(@RequestBody CouponCustomerCreateRequest request, String userId) {
        return ApiResponse.success();
    }

    // TODO userID 받아야된다.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{couponId}/verify")
    public ApiResponse<ReservationVerifyResponse> verify(@PathVariable String couponId, Integer reservationPrice) {
        return ApiResponse.success(ReservationVerifyResponse.builder()
                .reservationCouponId("dsknfsdvcxv")
                .discountedPrice(35000)
                .build());
    }

}
