package com.haot.coupon.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CheckAlreadyClosedEventDto {

    private String eventId;
    private String couponId;

}
