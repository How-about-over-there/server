package com.haot.lodge.application.service;

import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeRule;

public interface LodgeRuleService {

    LodgeRule getLodgeRuleByLodgeId(String lodgeId);

    void create(
            Lodge linkedLodge,
            Integer maxReservationDay,
            Integer maxPersonnel,
            String customization
    );
}
