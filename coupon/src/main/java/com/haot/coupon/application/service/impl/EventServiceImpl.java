package com.haot.coupon.application.service.impl;

import com.haot.coupon.application.dto.request.events.EventSearchRequest;
import com.haot.coupon.application.dto.response.PageResponse;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.coupon.application.mapper.EventMapper;
import com.haot.coupon.application.service.EventService;
import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.infrastructure.repository.CouponEventRepository;
import com.haot.submodule.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final CouponEventRepository couponEventRepository;
    private final EventMapper eventMapper;

    @Transactional(readOnly = true)
    @Override
    public EventSearchResponse getEvent(String eventId) {

        CouponEvent couponEvent = couponEventRepository.findById(eventId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_NOT_FOUND));

        return eventMapper.toSearchResponse(couponEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<EventSearchResponse> searchEvent(Role userRole, EventSearchRequest request, Pageable pageable) {

        if(userRole == Role.USER){

            // 사용자일때 삭제된 이벤트 보여주지 않는다.
            if(request.getIsDelete() == null || request.getIsDelete()){
                request.setDelete(false);
            }

            validateIfUserNonNullFields(request.getStartDate(), request.getEndDate(), request.getEventStatus());
        }

        return eventMapper.toPageResponse(couponEventRepository.searchEventByRole(request, pageable));
    }

    private void validateIfUserNonNullFields(LocalDateTime startDate, LocalDateTime endDate, String eventStatus) {
        boolean allFieldsNotNull = Stream.of(startDate, endDate, eventStatus)
                .anyMatch(Objects::nonNull);

        if (allFieldsNotNull) {
            throw new CustomCouponException(ErrorCode.INVALID_PARAMETERS_FOR_NON_ADMIN);
        }
    }

}
