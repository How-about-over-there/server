package com.haot.coupon.application.kafka;

public interface CouponErrorConsumer {

    public void eventErrorListener(String message);

}
