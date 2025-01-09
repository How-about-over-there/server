package com.haot.review.application.dtos.req;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ReviewCreateRequest(
    @NotBlank String contents,
    @NotBlank String lodgeId,
    List<MultipartFile> images
) {

}
