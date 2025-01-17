package com.haot.lodge.application.dto;


import com.haot.lodge.domain.model.Lodge;
import lombok.Builder;

@Builder
public record LodgeDto(
        String id,
        String hostId,
        String name,
        String description,
        String address,
        Integer term,
        Double basicPrice
) {
    public static LodgeDto from(Lodge lodge) {
        return new LodgeDto(
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
