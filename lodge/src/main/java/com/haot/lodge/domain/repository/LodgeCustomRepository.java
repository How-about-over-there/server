package com.haot.lodge.domain.repository;

import com.haot.lodge.domain.model.Lodge;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface LodgeCustomRepository {
    Slice<Lodge> findAllByConditionOf(
            Pageable pageable,
            String hostId,
            String name,
            String address,
            Integer maxReservationDay,
            Integer maxPersonnel,
            LocalDate checkInDate,
            LocalDate checkOutDate
    );
}
