package com.haot.review.application.dtos.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ReviewCreateRequest(
    @NotNull String contents,
    @NotNull String lodgeId,
    List<MultipartFile> images
) {

}
