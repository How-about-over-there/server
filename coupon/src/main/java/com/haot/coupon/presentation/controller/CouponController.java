package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.response.events.coupons.CouponReadMeResponse;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.domain.model.enums.CouponStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

    // TODO Header에서 userId 받아 사용
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public ApiResponse<List<CouponReadMeResponse>> getMyCoupons(String userId){

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



}
