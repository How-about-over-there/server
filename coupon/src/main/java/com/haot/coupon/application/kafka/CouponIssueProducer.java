package com.haot.coupon.application.kafka;

import com.haot.coupon.application.dto.CouponIssueDto;

public interface CouponIssueProducer {
    void sendIssuePriorityCoupon(CouponIssueDto couponIssueDto);

    void sendIssueUnlimitedCoupon(CouponIssueDto couponIssueDto);
}
