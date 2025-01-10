package com.haot.coupon.domain.utils;

import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class QueryDslSortUtils {

    public static OrderSpecifier<?>[] getOrderSpecifiers(
            Pageable pageable,
            Map<String, ComparableExpressionBase<?>> sortPaths
    ) {
        return pageable.getSort().stream()
                .map(order -> {
                    ComparableExpressionBase<?> sortPath = sortPaths.get(order.getProperty());
                    if (sortPath == null) {
                        throw new CustomCouponException(ErrorCode.UNSUPPORTED_SORT_TYPE);
                    }
                    return new OrderSpecifier<>(
                            order.isAscending()
                                    ? com.querydsl.core.types.Order.ASC
                                    : com.querydsl.core.types.Order.DESC,
                            sortPath
                    );
                })
                .toArray(OrderSpecifier[]::new);
    }

}
