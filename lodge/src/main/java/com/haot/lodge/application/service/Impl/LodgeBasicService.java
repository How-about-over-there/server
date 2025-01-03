package com.haot.lodge.application.service.Impl;


import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.repository.LodgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LodgeService {

    private final LodgeRepository lodgeRepository;

    public Lodge create(
            String userId,
            String name,
            String description,
            String address,
            Integer term,
            Double basicPrice
    ) {
        if(lodgeRepository.findByHostIdAndName(userId, name).isPresent()) {
            throw new LodgeException(ErrorCode.ALREADY_EXIST_REVIEW);
        }
        return Lodge.createLodge(
                userId, name, description, address, term, basicPrice
        );
    }
}
