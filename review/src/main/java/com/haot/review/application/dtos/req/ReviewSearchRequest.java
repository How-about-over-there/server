package com.haot.review.application.dtos.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 검색 요청 정보")
public record ReviewSearchRequest(

    @Schema(description = "숙소 ID", example = "lodge1234")
    String lodgeId,

    @Schema(description = "유저 ID", example = "user1234")
    String userId,

    @Schema(description = "삭제된 데이터", example = "true, false")
    @JsonProperty("deleted")
    Boolean isDeleted
) {

}
