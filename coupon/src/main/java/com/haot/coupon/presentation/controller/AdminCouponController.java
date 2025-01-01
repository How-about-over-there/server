package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.request.coupons.CouponCreateRequest;
import com.haot.coupon.application.dto.request.coupons.CouponSearchRequest;
import com.haot.coupon.application.dto.response.coupons.CouponCreateResponse;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/coupons")
public class AdminCouponController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<Page<CouponSearchResponse>> couponSearch(@ModelAttribute CouponSearchRequest request,
                                                                Pageable pageable) {

        List<CouponSearchResponse> response = List.of(
                CouponSearchResponse.builder()
                        .couponId("sadflkdsflksandf")
                        .couponName("테스트 쿠폰 1")
                        .couponAvailableDate(LocalDateTime.now().minusDays(1))
                        .couponExpiredDate(LocalDateTime.now().plusDays(1))
                        .couponType(CouponType.PRIORITY)
                        .discountPolicy(DiscountPolicy.PERCENTAGE)
                        .maximumAmount(500000)
                        .minimumAmount(50000)
                        .discountRate(10)
                        .maxQuantity(5000)
                        .issuedQuantity(3000)
                        .build(),
                CouponSearchResponse.builder()
                        .couponId("ankjdsfkjsdfj")
                        .couponName("테스트 쿠폰 2")
                        .couponAvailableDate(LocalDateTime.now().minusDays(2))
                        .couponExpiredDate(LocalDateTime.now().plusDays(2))
                        .couponType(CouponType.PRIORITY)
                        .discountPolicy(DiscountPolicy.AMOUNT)
                        .maximumAmount(500000)
                        .minimumAmount(50000)
                        .discountAmount(20000)
                        .maxQuantity(5000)
                        .issuedQuantity(3000)
                        .build()

        );

        return ApiResponse.success(new PageImpl<>(response, pageable, response.size()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ApiResponse<CouponCreateResponse> create(@RequestBody CouponCreateRequest request) {

        return ApiResponse.success(CouponCreateResponse.builder()
                .couponId("snckxnvjcxnvj")
                .build());
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{couponId}")
    public ApiResponse<Void> delete(@PathVariable String couponId) {
        return ApiResponse.success();
    }

}
