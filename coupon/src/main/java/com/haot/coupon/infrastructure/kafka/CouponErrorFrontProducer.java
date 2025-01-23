package com.haot.coupon.infrastructure.kafka;

import com.haot.coupon.application.dto.EventClosedDto;
import com.haot.coupon.application.kafka.CouponErrorProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponErrorFrontProducer implements CouponErrorProducer {

    private final KafkaTemplate<String, Object> errorKafkaTemplate;

    @Override
    public void sendEventClosed(EventClosedDto eventClosedDto) {

        errorKafkaTemplate.send(
                MessageBuilder.withPayload(eventClosedDto)
                        .setHeader(KafkaHeaders.TOPIC, "coupon-event-end")
                        .build())
                ;
    }

}
