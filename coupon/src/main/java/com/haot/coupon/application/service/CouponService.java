package com.haot.coupon.application.service;

import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.domain.model.enums.EventStatus;

public interface CouponService {
    void customerIssueCoupon(CouponCustomerCreateRequest request, String userId);

    void updateEndEventStatus(String eventId, EventStatus newStatus);
}
