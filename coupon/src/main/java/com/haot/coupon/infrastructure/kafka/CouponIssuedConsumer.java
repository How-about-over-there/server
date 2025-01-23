package com.haot.coupon.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.coupon.application.cache.RedisRepository;
import com.haot.coupon.application.dto.CouponIssueDto;
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

    private final CouponService couponService;

    @Override
    @KafkaListener(topics = "coupon-issue-priority", groupId = "coupon-user-priority",
            containerFactory = "priorityKafkaListenerContainerFactory")
    public void issuePriorityCouponListener(List<CouponIssueDto> requests,
                                            Acknowledgment acknowledgment) {
        /*TODO 여기서 List로 받으면 어떻게 해야될까..? for문 해서 여기서 DLQ, DLT 쓰기 어려울듯.. 재시도, 에러처리 안하는 거로..?*/
        batchCommit(requests, acknowledgment);
    }

    @Override
    @KafkaListener(topics = "coupon-issue-unlimited",
            groupId = "coupon-user-unlimited",
            containerFactory = "parallelKafkaListenerContainerFactory")
    public void batchIssueUnlimitedCouponListener(List<CouponIssueDto> requests,
                                                  Acknowledgment acknowledgment) {
        batchCommit(requests, acknowledgment);
    }


    private void batchCommit(List<CouponIssueDto> requests, Acknowledgment acknowledgment){
        couponService.batchIssueCoupon(requests);
        acknowledgment.acknowledge();
    }

}
