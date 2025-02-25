package com.haot.lodge.application.service;

import com.haot.lodge.application.dto.LodgeSearchCriteria;
import com.haot.lodge.domain.model.Lodge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface LodgeService {

    boolean isValidLodgeId(String lodgeId);

    Lodge getValidLodgeById(String lodgeId);

    Lodge create(
            String userId,
            String name,
            String description,
            String address,
            Integer term,
            Double basicPrice
    );

    Slice<Lodge> readAllBy(
            Pageable pageable,
            LodgeSearchCriteria searchCriteria
    );

    void update(
            Lodge lodge,
            String name,
            String description,
            String address,
            Integer term,
            Double basicPrice
    );

    void delete(String userId, Lodge lodge);

}
