package com.haot.review.application.dtos.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "리뷰 생성 요청 정보")
public record ReviewCreateRequest(
    @Schema(description = "리뷰 내용", example = "정말 좋은 숙소입니다!.")
    @NotBlank String contents,
    @Schema(description = "숙소 ID", example = "Lodge1234")
    @NotBlank String lodgeId,
    @Schema(description = "리뷰 이미지", example = "file1, file2, file3")
    List<MultipartFile> images
) {

}
