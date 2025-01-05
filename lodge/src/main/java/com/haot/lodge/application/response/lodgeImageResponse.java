package com.haot.lodge.application.response;


import lombok.Builder;

@Builder
public record LodgeImageResponse(
        String id,
        String title,
        String description,
        String url
){
}
