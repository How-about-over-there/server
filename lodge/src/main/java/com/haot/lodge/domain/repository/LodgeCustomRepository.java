package com.haot.lodge.domain.repository;

import com.haot.lodge.application.dto.LodgeSearchCriteria;
import com.haot.lodge.domain.model.Lodge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface LodgeCustomRepository {
    Slice<Lodge> findAllByConditionOf(
            Pageable pageable,
            LodgeSearchCriteria searchCriteria
    );
}
