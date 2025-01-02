package com.haot.coupon.domain.model.vo;

import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponDiscountRate {

    @Column(nullable = true, name = "discount_rate")
    private Integer rate;

    public CouponDiscountRate(Integer value) {

        if (value != null) {
            if (value < 1 || value > 100) {
                throw new CustomCouponException(ErrorCode.DISCOUNT_RATE_EXCEPTION);
            }
        }

        this.rate = value;
    }

}
