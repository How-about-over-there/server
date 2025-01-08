package com.haot.lodge.application.service.implement;


import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.repository.LodgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LodgeServiceImpl implements LodgeService {

    private final LodgeRepository lodgeRepository;

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
    public void update(
            Lodge lodge, String name, String description, String address, Integer term, Double basicPrice
    ) {
        nameValidation(lodge.getHostId(), name);
        lodge.update(name, description, address, term, basicPrice);
    }

    private void nameValidation(String hostId, String name) {
        if(lodgeRepository.findByHostIdAndName(hostId, name).isPresent()) {
            throw new LodgeException(ErrorCode.ALREADY_EXIST_LODGE_NAME);
        }
    }
}
