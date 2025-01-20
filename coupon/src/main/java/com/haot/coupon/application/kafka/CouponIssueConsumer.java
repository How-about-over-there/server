package com.haot.coupon.application.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haot.coupon.application.dto.CouponIssueDto;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

public interface CouponIssueConsumer {

    void issuePriorityCouponListener(List<CouponIssueDto> requests, Acknowledgment acknowledgment);

    void batchIssueUnlimitedCouponListener(List<CouponIssueDto> requests, Acknowledgment acknowledgment);
}
