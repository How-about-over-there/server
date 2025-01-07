package com.haot.coupon.infrastructure.kafka;

import com.haot.coupon.application.kafka.CouponErrorProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponErrorFrontProducer implements CouponErrorProducer {

    private final KafkaTemplate<String, String> errorKafkaTemplate;

    @Override
    public void sendEventExpired(String message) {

        errorKafkaTemplate.send(
                MessageBuilder.withPayload(message)
                        .setHeader(KafkaHeaders.TOPIC, "coupon-event-end")
                        .build())
                ;
    }

    @Override
    public void sendEventOutOfStock(String message) {

        errorKafkaTemplate.send(
                MessageBuilder.withPayload(message)
                        .setHeader(KafkaHeaders.TOPIC, "coupon-event-end")
                        .build())
                        ;
    }


}
