package com.haot.lodge.infrastructure.utils;

import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public class QuerydslSortUtils {

    public static OrderSpecifier<?>[] getOrderSpecifiers(
            Pageable pageable,
            Map<String, ComparableExpressionBase<?>> sortPaths
    ) {
        return pageable.getSort().stream()
                .map(order -> {
                    ComparableExpressionBase<?> sortPath = sortPaths.get(order.getProperty());
                    if (sortPath == null) {
                        throw new LodgeException(ErrorCode.UNSUPPORTED_SORT_TYPE);
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
