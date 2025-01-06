package com.haot.lodge.application.service.Impl;


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
public class LodgeRuleService {

    private final LodgeRuleRepository lodgeRuleRepository;

    @Transactional
    public void create(
            Lodge linkedLodge, Integer maxReservationDay, Integer maxPersonnel, String customization
    ) {
        lodgeRuleRepository.save(
                LodgeRule.create(linkedLodge, maxReservationDay, maxPersonnel, customization)
        );
    }

    public LodgeRule getLodgeRuleByLodgeId(String lodgeId) {
        return lodgeRuleRepository.findLodgeRuleByLodgeId(lodgeId)
                .orElseThrow(()->new LodgeException(ErrorCode.LODGE_RULE_NOT_FOUND));
    }
}
