package com.haot.review.application.dtos.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReviewSearchRequest(
    String lodgeId,

    String userId,
    @JsonProperty("deleted")
    Boolean isDeleted
) {

}
