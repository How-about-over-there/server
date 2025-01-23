package com.haot.coupon.application.dto;

import com.haot.coupon.domain.model.enums.EventStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventClosedDto {

    private String eventId;
    private EventStatus status;

}
