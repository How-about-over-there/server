package com.haot.coupon.application.mapper;

import com.haot.coupon.application.dto.request.coupons.CouponCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponCreateResponse;
import com.haot.coupon.domain.model.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CouponMapper {


    @Mapping(source = "couponName", target = "name")
    @Mapping(source = "couponAvailableDate", target = "availableDate")
    @Mapping(source = "couponExpiredDate", target = "expiredDate")
    @Mapping(source = "couponType", target = "type")
    @Mapping(source = "minDiscountAmount", target = "minAvailableAmount")
    @Mapping(source = "maxDiscountAmount", target = "maxAvailableAmount")
    @Mapping(source = "discountRate", target = "discountRate.rate")
    @Mapping(source = "discountAmount", target = "discountAmount")
    Coupon toEntity(CouponCreateRequest couponCreateRequest);

    CouponCreateResponse responseId(String couponId);

}
