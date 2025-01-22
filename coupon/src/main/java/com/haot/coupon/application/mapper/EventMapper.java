package com.haot.coupon.application.mapper;

import com.haot.coupon.application.dto.EventClosedDto;
import com.haot.coupon.application.dto.request.events.EventCreateRequest;
import com.haot.coupon.application.dto.response.PageResponse;
import com.haot.coupon.application.dto.response.events.EventCreateResponse;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.model.enums.EventStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.io.Serializable;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "coupon.id", source = "coupon.id")
    @Mapping(target = "description", source = "request.eventDescription")
    @Mapping(target = "eventStatus", constant = "DEFAULT")
    @Mapping(target = "isDelete", ignore = true)
    CouponEvent toEntity(EventCreateRequest request, Coupon coupon);

    @Mapping(target = "eventId", source = "id")
    @Mapping(target = "couponId", source = "coupon.id")
    EventCreateResponse toCreateResponse(CouponEvent event);

    @Mapping(target = "eventId", source = "id")
    @Mapping(target = "couponId", source = "coupon.id")
    @Mapping(target = "eventDescription", source = "description")
    EventSearchResponse toSearchResponse(CouponEvent event);

    @Mapping(source = "id", target = "eventId")
    @Mapping(source = "coupon.id", target = "couponId")
    @Mapping(source = "eventName", target = "eventName")
    @Mapping(source = "description", target = "eventDescription")
    EventSearchResponse toEventResponseDTO(CouponEvent event);

    EventClosedDto toProduce(String eventId, EventStatus status);

    default <T extends Serializable> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getContent()
        );
    }
}
