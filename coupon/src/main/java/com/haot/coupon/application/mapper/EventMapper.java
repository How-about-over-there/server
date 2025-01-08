package com.haot.coupon.application.mapper;

import com.haot.coupon.application.dto.request.events.EventCreateRequest;
import com.haot.coupon.application.dto.response.events.EventCreateResponse;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "coupon.id", source = "coupon.id")
    @Mapping(target = "description", source = "request.eventDescription")
    CouponEvent toEntity(EventCreateRequest request, Coupon coupon);

    @Mapping(target = "eventId", source = "id")
    @Mapping(target = "couponId", source = "coupon.id")
    EventCreateResponse toCreateResponse(CouponEvent event);

    @Mapping(target = "eventId", source = "id")
    @Mapping(target = "couponId", source = "coupon.id")
    @Mapping(target = "eventDescription", source = "description")
    EventSearchResponse toSearchResponse(CouponEvent event);

}
