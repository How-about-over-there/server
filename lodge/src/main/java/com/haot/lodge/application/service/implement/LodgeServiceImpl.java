package com.haot.lodge.application.service.implement;


import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.repository.LodgeRepository;
import java.time.LocalDate;
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
        return lodgeRepository.findById(lodgeId)
                .orElseThrow(()->new LodgeException(ErrorCode.LODGE_NOT_FOUND));
        // TODO: isDeleted = true 인 경우 제외되도록 해야 함
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
            Pageable pageable,
            String hostId, String name, String address,
            Integer maxReservationDay, Integer maxPersonnel,
            LocalDate checkInDate, LocalDate checkOutDate
    ) {
        if(checkInDate!= null && checkOutDate == null){
            checkOutDate = checkInDate.plusDays(1);
        }
        return lodgeRepository
                .findAllByConditionOf(
                        pageable, hostId, name, address, maxReservationDay, maxPersonnel, checkInDate, checkOutDate
                );
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
