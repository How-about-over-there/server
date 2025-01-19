package com.haot.coupon.infrastructure.kafka;

import com.haot.coupon.application.dto.EventClosedDto;
import com.haot.coupon.application.kafka.CouponErrorConsumer;
import com.haot.coupon.application.service.CouponService;
import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.domain.model.enums.EventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j(topic = "CouponErrorFrontListener")
@RequiredArgsConstructor
public class CouponErrorFrontConsumer implements CouponErrorConsumer {

    private final CouponService couponService;

    @KafkaListener(topics = "coupon-event-end", groupId = "coupon-event-error",
            containerFactory = "kafkaListenerContainerFactory")
    public void eventErrorListener(List<EventClosedDto> eventClosedDtoList, Acknowledgment acknowledgment) {

        // Front 개발자가 front db에 저장을 한다는 가정, 이벤트 종료 처리해달라는 메시지를 받았다는 가정, 요청을 줄이기 위한 consumer
//        log.info("Received Message: {}", message);
//
//        String[] messageArr = message.split(" ");
//
//        EventStatus status = EventStatus.checkEventStatus(messageArr[0]);

        // 중복 제거
        Set<EventClosedDto> uniqueEvents = new HashSet<>(eventClosedDtoList);

        couponService.updateEndEventStatus(uniqueEvents);

        acknowledgment.acknowledge();


        // 원래 front로 보내는 용도로 쓰이는 곳 ->
        // TODO 여기서 set으로 돌아 log를 찍어줘야 하나? 근데 예를 들어 같은 eventId에 다른 eventstatus 종료값이 3개 있을때?
        // TODO 뭔가 애매해 졌다.. front로 보내 이벤트 정료 처리해 달라는 메시지를 받았다는 가정하기
        // 굳이 신경 안써도 되나?
//        switch (status) {
//            case EXPIRED -> log.info("eventId : {}, 이벤트 종료.", messageArr[1]);
//            case OUT_OF_STOCK -> log.info("eventId : {}, 쿠폰 재고 소진으로 인한 이벤트 종료.", messageArr[1]);
//            case MANUALLY_CLOSED -> log.info("eventId : {}, 관리자에 의한 이벤트 강제 종료.", messageArr[1]);
//        }

    }

}
