package com.haot.lodge.application.response;


import com.haot.lodge.domain.model.LodgeImage;
import lombok.Builder;

@Builder
public record LodgeImageResponse(
        String id,
        String title,
        String description,
        String url
){
    public static LodgeImageResponse from(LodgeImage lodgeImage) {
        return new LodgeImageResponse(
                lodgeImage.getId(),
                lodgeImage.getTitle(),
                lodgeImage.getDescription(),
                lodgeImage.getUrl()
        );
    }
}
