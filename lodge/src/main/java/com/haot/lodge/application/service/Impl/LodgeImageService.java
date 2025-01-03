package com.haot.lodge.application.service;


import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeImage;
import com.haot.lodge.domain.repository.LodgeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class LodgeImageService {

    private final LodgeImageRepository lodgeImageRepository;

    public void create(
            Lodge lodge, MultipartFile file, String title, String description
    ) {
        lodgeImageRepository.save(
                LodgeImage.create(lodge, convertToUrl(file), title, description)
        );
    }

    private String convertToUrl(MultipartFile file) {
        return "http://S3";
    }
}
