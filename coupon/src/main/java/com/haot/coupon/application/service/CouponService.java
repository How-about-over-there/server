package com.haot.coupon.application.service;

import com.haot.coupon.application.dto.feign.request.FeignVerifyRequest;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.application.dto.feign.response.ReservationVerifyResponse;
import com.haot.coupon.domain.model.enums.EventStatus;

public interface CouponService {
    void customerIssueCoupon(CouponCustomerCreateRequest request, String userId);

    void updateEndEventStatus(String eventId, EventStatus newStatus);

    CouponSearchResponse getCouponDetails(String couponId);

    ReservationVerifyResponse verify(FeignVerifyRequest request);
}
