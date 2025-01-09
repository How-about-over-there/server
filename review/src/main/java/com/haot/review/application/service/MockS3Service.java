package com.haot.review.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MockS3Service {

  // 이미지를 업로드하고 URL을 반환하는 모의 메서드
  public List<String> uploadImages(List<MultipartFile> images) {
    return images.stream()
        .map(image -> "https://mock-s3.com/" + image.getOriginalFilename())
        .toList();
  }
}
