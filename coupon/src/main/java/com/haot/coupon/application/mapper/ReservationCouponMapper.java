package com.haot.coupon.application.mapper;

import com.haot.coupon.application.dto.response.coupons.ReservationVerifyResponse;
import com.haot.coupon.domain.model.ReservationCoupon;
import com.haot.coupon.domain.model.UserCoupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationCouponMapper {

    @Mapping(target = "userCoupon.id", source = "userCoupon.id")
    @Mapping(target = "reservationPrice", source = "totalPrice")
    @Mapping(target = "reservationDiscountPrice", source = "discountPrice")
    @Mapping(target = "reservationCouponStatus", constant = "PREEMPTION")
    ReservationCoupon toEntity(UserCoupon userCoupon, double totalPrice, double discountPrice);

    @Mapping(target = "reservationCouponId", source = "id")
    @Mapping(target = "discountedPrice", source = "discountedPrice")
    ReservationVerifyResponse toVerifyFeignResponse(String id, double discountedPrice);
}
