package com.haot.coupon.application.kafka;

import com.haot.coupon.application.dto.EventClosedDto;

public interface CouponErrorProducer {

    void sendEventClosed(EventClosedDto eventClosedDto);

}
