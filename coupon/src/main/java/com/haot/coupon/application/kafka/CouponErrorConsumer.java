package com.haot.coupon.application.kafka;

public interface CouponErrorConsumer {

    void eventErrorListener(String message);

}
