package com.haot.coupon.application.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haot.coupon.application.dto.UnlimitedCouponDto;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

public interface CouponIssueConsumer {

    void issuePriorityCouponListener(String userId, String message, Acknowledgment acknowledgment) throws JsonProcessingException;

    void batchIssueUnlimitedCouponListener(List<UnlimitedCouponDto> requests, Acknowledgment acknowledgment);

    void batchIssueUnlimitedCouponListener1(List<UnlimitedCouponDto> requests, Acknowledgment acknowledgment);

    void batchIssueUnlimitedCouponListener2(List<UnlimitedCouponDto> requests, Acknowledgment acknowledgment);
}
