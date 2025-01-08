package com.haot.coupon.domain.model.enums;

import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum EventStatus {

    DEFAULT("기본값"),
    // 쿠폰 발급 수량과 무관하게 기한이 지나 종료
    EXPIRED("기한이 지나 종료"),
    OUT_OF_STOCK("쿠폰 발급 수량으로 인한 종료"),
    MANUALLY_CLOSED("관리자가 종료")
    ;

    private final String description;

    public static EventStatus checkEventStatus(String status) {
        return Stream.of(EventStatus.values())
                .filter(eventStatus -> eventStatus.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_STATUS_NOT_MATCH));
    }

}
