package com.haot.coupon.application.kafka;

public interface CouponErrorProducer {

    public void sendEventExpired(String message);

    public void sendEventOutOfStock(String message);

}
