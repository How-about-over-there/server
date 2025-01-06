package com.haot.lodge.presentation.response;

import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.response.LodgeImageResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record LodgeReadAllResponse(
        LodgeResponse lodge,
        List<LodgeImageResponse> images
) {
}
