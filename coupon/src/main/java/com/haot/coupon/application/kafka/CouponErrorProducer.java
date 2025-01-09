package com.haot.coupon.application.kafka;

public interface CouponErrorProducer {

    void sendEventClosed(String message);

}
