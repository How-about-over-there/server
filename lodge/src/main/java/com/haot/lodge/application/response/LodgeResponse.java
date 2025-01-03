package com.haot.lodge.application.response;


import com.haot.lodge.domain.model.Lodge;
import lombok.Builder;

@Builder
public record LodgeResponse(
        String id,
        String hostId,
        String name,
        String description,
        String address,
        Integer term,
        Double basicPrice
) {
    public static LodgeResponse from(Lodge lodge) {
        return new LodgeResponse(
                lodge.getId(),
                lodge.getHostId(),
                lodge.getName(),
                lodge.getDescription(),
                lodge.getAddress(),
                lodge.getTerm(),
                lodge.getBasicPrice()
        );
    }
}
