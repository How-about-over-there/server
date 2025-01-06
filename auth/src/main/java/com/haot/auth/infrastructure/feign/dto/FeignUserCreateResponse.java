package com.haot.auth.infrastructure.feign.dto;

import lombok.Builder;

@Builder
public record FeignUserCreateResponse(
    String userId
) {

}
