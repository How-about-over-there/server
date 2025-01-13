package com.haot.coupon.application.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.messaging.handler.annotation.Header;

public interface CouponIssueConsumer {

    void issuePriorityCouponListener(@Header("X-User-Id") String userId, String message) throws JsonProcessingException;
}
