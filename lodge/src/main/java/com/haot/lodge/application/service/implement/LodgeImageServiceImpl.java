package com.haot.lodge.application.service.implement;


import com.haot.lodge.application.service.LodgeImageService;
import com.haot.lodge.common.utils.S3Service;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeImage;
import com.haot.lodge.domain.repository.LodgeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class LodgeImageServiceImpl implements LodgeImageService {

    private final S3Service s3Service;
    private final LodgeImageRepository lodgeImageRepository;

    @Override
    public void create(
            Lodge lodge, MultipartFile file, String title, String description
    ) {
        lodgeImageRepository.save(
                LodgeImage.create(lodge, s3Service.convertToUrl(file), title, description)
        );
    }

    @Override
    public void deleteAllByLodge(Lodge lodge, String userId) {
        lodge.getImages().forEach(image -> image.deleteEntity(userId));
    }

}
