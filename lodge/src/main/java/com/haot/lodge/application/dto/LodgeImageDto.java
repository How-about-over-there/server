package com.haot.lodge.application.dto;


import com.haot.lodge.domain.model.LodgeImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "숙소 이미지 정보")
@Builder
public record LodgeImageDto(
        String id,
        String title,
        String description,
        String url
){
    public static LodgeImageDto from(LodgeImage lodgeImage) {
        return new LodgeImageDto(
                lodgeImage.getId(),
                lodgeImage.getTitle(),
                lodgeImage.getDescription(),
                lodgeImage.getUrl()
        );
    }
}
