package com.haot.coupon.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.coupon.application.cache.RedisRepository;
import com.haot.coupon.application.dto.UnlimitedCouponDto;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.kafka.CouponIssueConsumer;
import com.haot.coupon.application.service.CouponService;
import com.haot.coupon.common.exceptions.CustomCouponException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j(topic = "CouponIssuedConsumer")
@RequiredArgsConstructor
public class CouponIssuedConsumer implements CouponIssueConsumer {

    private final ObjectMapper objectMapper;
    private final CouponService couponService;
    private final RedisRepository redisRepository;

    @Override
    @KafkaListener(topics = "coupon-issue-priority", groupId = "coupon-user-priority")
    public void issuePriorityCouponListener(@Header("X-User-Id") String userId, String message,
                                            Acknowledgment acknowledgment) throws JsonProcessingException {

        CouponCustomerCreateRequest request = objectMapper.readValue(message, CouponCustomerCreateRequest.class);

        try {
            couponService.issueCoupon(userId, request);
            acknowledgment.acknowledge();
        } catch (CustomCouponException ex) {
            if (ex.resCode.getCode().equals("4016")) {

                if (redisRepository.getCouponQuantityByIds(request.eventId(), request.couponId()) != null) {
                    redisRepository.increaseCouponQuantity(request.eventId(), request.couponId());
                }
            }
        }
    }

    @Override
    @KafkaListener(topics = "coupon-issue-unlimited",
            groupId = "coupon-user-unlimited",
            containerFactory = "parallelKafkaListenerContainerFactory")
    public void batchIssueUnlimitedCouponListener(List<UnlimitedCouponDto> requests,
                                                  Acknowledgment acknowledgment) {
        couponService.batchIssueCoupon(requests);
        acknowledgment.acknowledge();
    }

    @Override
    @KafkaListener(topics = "coupon-issue-unlimited",
            groupId = "coupon-user-unlimited",
            containerFactory = "parallelKafkaListenerContainerFactory")
    public void batchIssueUnlimitedCouponListener1(List<UnlimitedCouponDto> requests,
                                                  Acknowledgment acknowledgment) {
        couponService.batchIssueCoupon(requests);
        acknowledgment.acknowledge();
    }

    @Override
    @KafkaListener(topics = "coupon-issue-unlimited",
            groupId = "coupon-user-unlimited",
            containerFactory = "parallelKafkaListenerContainerFactory")
    public void batchIssueUnlimitedCouponListener2(List<UnlimitedCouponDto> requests,
                                                  Acknowledgment acknowledgment) {
        couponService.batchIssueCoupon(requests);
        acknowledgment.acknowledge();
    }

}
