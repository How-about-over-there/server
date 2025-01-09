package com.haot.review.application.dtos.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearchRequest {

    private String lodgeId;

    private String userId;
    @JsonProperty("deleted")
    private boolean isDeleted;

}
