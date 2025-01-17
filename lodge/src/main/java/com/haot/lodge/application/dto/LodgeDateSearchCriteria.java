package com.haot.lodge.application.dto;

import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.presentation.request.LodgeDateSearchParams;
import java.time.LocalDate;

public record LodgeDateSearchCriteria(
        Lodge lodge,
        LocalDate start,
        LocalDate end
) {
    public static LodgeDateSearchCriteria of(Lodge lodge, LodgeDateSearchParams params) {
        return new LodgeDateSearchCriteria(
                lodge,
                params.start(),
                params.end()
        );
    }
}
