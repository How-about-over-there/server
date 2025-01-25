package com.haot.review.infrastructure.dto;

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
