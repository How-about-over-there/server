package com.haot.lodge.application.response;


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
}
