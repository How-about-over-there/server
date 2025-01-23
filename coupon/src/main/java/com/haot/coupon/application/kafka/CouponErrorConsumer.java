package com.haot.coupon.application.kafka;

import com.haot.coupon.application.dto.EventClosedDto;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

public interface CouponErrorConsumer {

    void eventErrorListener(List<EventClosedDto> eventClosedDtoList, Acknowledgment acknowledgment);

}
