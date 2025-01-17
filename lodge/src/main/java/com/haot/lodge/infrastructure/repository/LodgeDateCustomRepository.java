package com.haot.lodge.infrastructure.repository;

import com.haot.lodge.application.dto.LodgeDateSearchCriteria;
import com.haot.lodge.domain.model.LodgeDate;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LodgeDateCustomRepository {

    boolean existsOverlappingDates(
            String lodgeId, LocalDate startDate, LocalDate endDate
    );

    Slice<LodgeDate> findAllDateByConditionOf(
            Pageable pageable, LodgeDateSearchCriteria searchCriteria
    );
}
