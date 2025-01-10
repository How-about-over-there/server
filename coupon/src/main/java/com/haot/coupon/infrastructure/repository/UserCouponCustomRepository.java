package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.application.dto.response.coupons.CouponReadMeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCouponCustomRepository {
    Page<CouponReadMeResponse> checkMyCouponBox(String userId, Pageable pageable);
}
