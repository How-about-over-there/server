package com.haot.review.application.dtos.req;

import jakarta.validation.constraints.NotBlank;

public record ReviewUpdateRequest(
    @NotBlank String contents
) {
}
