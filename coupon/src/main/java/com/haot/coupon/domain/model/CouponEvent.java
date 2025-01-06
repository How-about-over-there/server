package com.haot.coupon.domain.model;

import com.haot.coupon.domain.model.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_coupon_event")
public class CouponEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // 추후 쿠폰을 돌려쓸 수도 있으니 Many to One으로
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false, name = "event_start_date")
    private LocalDateTime eventStartDate;

    @Column(nullable = false, name = "event_end_date")
    private LocalDateTime eventEndDate;

    @Column(nullable = false, name = "event_name")
    private String eventName;

    @Column(nullable = false, name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus eventStatus;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete = false;


    public void updateEventStatus(){

        LocalDateTime now = LocalDateTime.now();

        this.eventStatus = EventStatus.DEFAULT;

        if(now.isAfter(this.eventEndDate)){
            this.eventStatus = EventStatus.EXPIRED;

        }

    }

}
