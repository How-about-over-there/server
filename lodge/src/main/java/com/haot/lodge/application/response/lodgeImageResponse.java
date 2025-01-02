package com.haot.lodge.application.response;


import lombok.Builder;

@Builder
public record lodgeImageResponse(
        String id,
        String title,
        String description,
        String url
){
}
