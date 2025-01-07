package com.haot.lodge.application.service;

import com.haot.lodge.domain.model.Lodge;
import org.springframework.web.multipart.MultipartFile;

public interface LodgeImageService {

    void create(
            Lodge lodge,
            MultipartFile file,
            String title,
            String description
    );
}
