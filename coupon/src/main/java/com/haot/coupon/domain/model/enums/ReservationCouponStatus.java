package com.haot.coupon.domain.model.enums;

import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ReservationCouponStatus {

    CANCEL("쿠폰 적용 취소"),
    COMPLETED("쿠폰 적용 완료"),
    PREEMPTION("쿠폰 선점"),
    EXPIRED("쿠폰 만료");

    private final String description;

    // String 값으로 해당하는 Enum 값을 반환하는 메서드
    public static CouponStatus checkReservationCouponStatus(String status) {
        return Stream.of(CouponStatus.values())
                .filter(couponStatus -> couponStatus.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new CustomCouponException(ErrorCode.RESERVATION_STATUS_NOT_MATCH));
    }

}
