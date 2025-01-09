package com.haot.coupon.infrastructure.kafka;

import com.haot.coupon.application.kafka.CouponErrorConsumer;
import com.haot.coupon.application.service.CouponService;
import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.domain.model.enums.EventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "CouponErrorFrontListener")
@RequiredArgsConstructor
public class CouponErrorFrontConsumer implements CouponErrorConsumer {

    private final CouponService couponService;

    @KafkaListener(topics = "coupon-event-end", groupId = "coupon-event-error")
    public void eventErrorListener(String message){

        // Front 개발자가 front db에 저장을 한다는 가정, 이벤트 종료 처리해달라는 메시지를 받았다는 가정, 요청을 줄이기 위한 consumer
        log.info("Received Message: {}", message);

        String[] messageArr = message.split(" ");

        EventStatus status = EventStatus.checkEventStatus(messageArr[0]);

        try{
            couponService.updateEndEventStatus(messageArr[1], status);
        }catch(CustomCouponException ex){
            log.error("{} ,eventId : {}", ex.resCode.getMessage(), messageArr[1]);
            return;
        }

        // 원래 front로 보내는 용도로 쓰이는 곳
        switch (status){
            case EXPIRED -> log.info("eventId : {}, 이벤트 종료.", messageArr[1]);
            case OUT_OF_STOCK -> log.info("eventId : {}, 쿠폰 재고 소진으로 인한 이벤트 종료.", messageArr[1]);
            case MANUALLY_CLOSED -> log.info("eventId : {}, 관리자에 의한 이벤트 강제 종료.", messageArr[1]);
        }

    }

}
