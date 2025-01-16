package com.haot.coupon.application.kafka;

import com.haot.coupon.application.dto.UnlimitedCouponDto;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;

public interface CouponIssueProducer {
    void sendIssuePriorityCoupon(String userId, CouponCustomerCreateRequest request);

    void sendIssueUnlimitedCoupon(UnlimitedCouponDto unlimitedCouponDto);
}
