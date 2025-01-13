package com.haot.coupon.application.dto.request.events;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSearchRequest {

    // 유저가 보낼 수 있는 것 (삭제되지 않은, start date < 현재 < end Date, 이벤트 상태값이 DEFAULT인 이벤트만 조회)
    private String nameKeyword;
    private String descriptionKeyword;

    // 나머지는 다 Admin만 보낼 수 있게끔
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isDelete;   // 삭제된 이벤트 여부

    @Pattern(regexp = "DEFAULT|MANUALLY_CLOSED|EXPIRED|OUT_OF_STOCK", message = "유효한 상태 값을 입력하세요.")
    private String eventStatus;

    public void setDelete(Boolean delete) {
        this.isDelete = delete;
    }
}
