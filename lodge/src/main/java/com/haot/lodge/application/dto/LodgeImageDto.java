package com.haot.lodge.application.dto;


import com.haot.lodge.domain.model.LodgeImage;
import lombok.Builder;

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
