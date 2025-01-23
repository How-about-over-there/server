package com.haot.coupon.domain.utils;

import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CouponIssueRedisCode {
    SUCCESS(1), // 발급 성공
    ALREADY_ISSUED(2), // 이미 발급된 유저
    EXCEEDED_LIMIT(3); // 발급 수량 초과

    private final Integer code;

    public static CouponIssueRedisCode checkCouponIssueRedisCode(String code) {
        CouponIssueRedisCode redisCode = Stream.of(CouponIssueRedisCode.values())
                .filter(codeEnum -> codeEnum.getCode().equals(Integer.parseInt(code)))
                .findFirst()
                .orElseThrow(() -> new CustomCouponException(ErrorCode.NOT_FOUND_EXCEPTION));

        if (redisCode.getCode() == 2) {
            throw new CustomCouponException(ErrorCode.DUPLICATED_ISSUED_COUPON);
        }

        return redisCode;
    }
}
