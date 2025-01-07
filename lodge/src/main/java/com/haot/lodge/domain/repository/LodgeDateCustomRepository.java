package com.haot.lodge.domain.repository;

import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LodgeDateCustomRepository {
    Slice<LodgeDate> findAllLodgeDateByRange(
            Pageable pageable, Lodge lodge, LocalDate start, LocalDate end
    );
}
