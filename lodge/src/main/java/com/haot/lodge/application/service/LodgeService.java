package com.haot.lodge.application.service;

import com.haot.lodge.domain.model.Lodge;


public interface LodgeService {

    Lodge getValidLodgeById(String lodgeId);

    Lodge create(
            String userId,
            String name,
            String description,
            String address,
            Integer term,
            Double basicPrice
    );

    void update(
            Lodge lodge,
            String name,
            String description,
            String address,
            Integer term,
            Double basicPrice
    );

}
