package com.haot.coupon.application.kafka;

public interface CouponErrorProducer {

    void sendEventExpired(String message);

    void sendEventOutOfStock(String message);

}
