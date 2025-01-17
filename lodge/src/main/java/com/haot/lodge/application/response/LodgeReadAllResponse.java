package com.haot.lodge.application.response;

import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.dto.LodgeImageDto;
import java.util.List;

public record LodgeReadAllResponse(
        LodgeDto lodge,
        List<LodgeImageDto> images
) {
}
