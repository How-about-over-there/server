package com.haot.coupon.infrastructure.kafka;

import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.kafka.CouponIssueProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssuedProducer implements CouponIssueProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendIssuePriorityCoupon(String userId, CouponCustomerCreateRequest request) {

        kafkaTemplate.send(
                MessageBuilder.withPayload(request)
                        .setHeader(KafkaHeaders.TOPIC, "coupon-issue-priority")
                        .setHeader("X-User-Id", userId)
                        .build())
        ;
    }

    @Override
    public void sendIssueUnlimitedCoupon(String userId, CouponCustomerCreateRequest request) {

        kafkaTemplate.send(
                MessageBuilder.withPayload(request)
                        .setHeader(KafkaHeaders.TOPIC, "coupon-issue-unlimited")
                        .setHeader("X-User-Id", userId)
                        .build())
        ;
    }
}
