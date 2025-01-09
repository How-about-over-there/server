package com.haot.review.presentation.dto;

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
