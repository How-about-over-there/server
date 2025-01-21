package com.haot.coupon.presentation.controller;

import com.haot.coupon.application.dto.request.coupons.CouponCreateRequest;
import com.haot.coupon.application.dto.request.coupons.CouponSearchRequest;
import com.haot.coupon.application.dto.response.coupons.CouponCreateResponse;
import com.haot.coupon.application.dto.response.coupons.CouponHistoryResponse;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.application.service.AdminCouponService;
import com.haot.coupon.common.response.ApiResponse;
import com.haot.coupon.common.response.enums.SuccessCode;
import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;
import com.haot.coupon.domain.model.enums.ReservationCouponStatus;
import com.haot.coupon.domain.model.vo.CouponDiscountRate;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
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

    private final AdminCouponService adminCouponService;

    // 쿠폰 생성 API
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @RoleCheck(Role.ADMIN)
    public ApiResponse<CouponCreateResponse> create(@Valid @RequestBody CouponCreateRequest request) {
        return ApiResponse.SUCCESS(SuccessCode.CREATE_COUPON_SUCCESS, adminCouponService.create(request));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{couponId}")
    public ApiResponse<Void> delete(@PathVariable String couponId) {
        return ApiResponse.success();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/reservation/{reservationCouponId}")
    public ApiResponse<Void> deleteCouponHistory(@PathVariable(value = "reservationCouponId") String reservationCouponId) {
        return ApiResponse.success();
    }

}
