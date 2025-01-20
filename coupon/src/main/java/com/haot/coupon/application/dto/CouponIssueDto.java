package com.haot.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueDto {

    private String eventId;
    private String couponId;
    private String userId;
}
