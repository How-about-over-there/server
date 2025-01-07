package com.haot.lodge.application.service.implement;


import com.haot.lodge.application.service.LodgeRuleService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeRule;
import com.haot.lodge.domain.repository.LodgeRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LodgeRuleServiceImpl implements LodgeRuleService {

    private final LodgeRuleRepository lodgeRuleRepository;

    @Override
    public LodgeRule getLodgeRuleByLodgeId(String lodgeId) {
        return lodgeRuleRepository.findLodgeRuleByLodgeId(lodgeId)
                .orElseThrow(()->new LodgeException(ErrorCode.LODGE_RULE_NOT_FOUND));
    }

    @Override
    public void create(
            Lodge linkedLodge, Integer maxReservationDay, Integer maxPersonnel, String customization
    ) {
        lodgeRuleRepository.save(
                LodgeRule.create(linkedLodge, maxReservationDay, maxPersonnel, customization)
        );
    }

}
