package com.haot.coupon.application.service;

import com.haot.coupon.application.dto.request.coupons.CouponCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponCreateResponse;
import jakarta.validation.Valid;

public interface AdminCouponService {

    CouponCreateResponse create(CouponCreateRequest request);
}
