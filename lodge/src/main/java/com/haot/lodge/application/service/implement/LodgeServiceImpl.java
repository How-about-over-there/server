package com.haot.lodge.application.service.implement;


import com.haot.lodge.application.dto.LodgeSearchCriteria;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.repository.LodgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LodgeServiceImpl implements LodgeService {

    private final LodgeRepository lodgeRepository;

    @Override
    public boolean isValidLodgeId(String lodgeId) {
        return lodgeRepository.existsByIdAndIsDeletedFalse(lodgeId);
    }

    @Override
    public Lodge getValidLodgeById(String lodgeId) {
        return lodgeRepository.findByIdAndIsDeletedFalse(lodgeId)
                .orElseThrow(()->new LodgeException(ErrorCode.LODGE_NOT_FOUND));
    }

    @Override
    public Lodge create(
            String userId, String name, String description, String address, Integer term, Double basicPrice
    ) {
        nameValidation(userId, name);
        return lodgeRepository.save(Lodge.createLodge(
                userId, name, description, address, term, basicPrice
        ));
    }

    @Override
    public Slice<Lodge> readAllBy(
            Pageable pageable, LodgeSearchCriteria searchCriteria
    ) {
        return lodgeRepository.findAllByConditionOf(pageable, searchCriteria);
    }

    @Override
    public void update(
            Lodge lodge, String name, String description, String address, Integer term, Double basicPrice
    ) {
        nameValidation(lodge.getHostId(), name);
        lodge.update(name, description, address, term, basicPrice);
    }

    @Override
    public void delete(String userId, Lodge lodge) {
        lodge.deleteEntity(userId);
    }

    private void nameValidation(String hostId, String name) {
        if(lodgeRepository.findByHostIdAndName(hostId, name).isPresent()) {
            throw new LodgeException(ErrorCode.ALREADY_EXIST_LODGE_NAME);
        }
    }
}
