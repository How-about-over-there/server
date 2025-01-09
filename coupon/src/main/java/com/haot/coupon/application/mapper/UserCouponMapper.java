package com.haot.coupon.application.mapper;

import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.UserCoupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserCouponMapper {

    @Mapping(target = "coupon.id", source = "coupon.id")
    @Mapping(target = "couponStatus", constant = "DISTRIBUTED")
    @Mapping(target = "usedDate", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    UserCoupon toEntity(String userId, Coupon coupon);


}
