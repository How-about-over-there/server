package com.haot.lodge.domain.model;

import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.submodule.auditor.BaseEntity;
import com.haot.submodule.role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_lodge_date", schema = "lodge")
public class LodgeDate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodge_id")
    private Lodge lodge;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public void updatePrice(Double price) {
        this.price = price;
    }

    public void updateStatus(ReservationStatus status) {
        if(this.status == status) {
            throw new LodgeException(ErrorCode.ALREADY_CHANGED_DATE_STATUS);
        }
        this.status = status;
    }

    public static LodgeDate create(
            Lodge lodge, LocalDate date, Double price, ReservationStatus status
    ) {
        return LodgeDate.builder()
                .lodge(lodge)
                .date(date)
                .price(price)
                .status(ReservationStatus.EMPTY)
                .build();
    }

    public void verifyProperty(Role role, String userId) {
        if(role==Role.HOST && !this.lodge.getHostId().equals(userId))
            throw new LodgeException(ErrorCode.FORBIDDEN_ACCESS_LODGE);
    }
}
