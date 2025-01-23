package com.haot.review.application.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.haot.review.common.exceptions.CustomReviewException;
import com.haot.review.common.response.enums.ErrorCode;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  private final AmazonS3Client S3Client;

  public String convertToUrl(final MultipartFile file) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());
    metadata.setContentLength(file.getSize());
    String fileName = getRandomFileName(file);
    try {
      S3Client.putObject(
          new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
      );
    } catch (IOException e) {
      throw new CustomReviewException(ErrorCode.FIlE_UPLOAD_FAILED);
    }
    return getFileUrlFromS3(fileName);
  }

  private String getFileUrlFromS3(final String fileName) {
    return S3Client.getUrl(bucket, fileName).toString();
  }

  private String getRandomFileName(final MultipartFile file) {
    String randomUUID = UUID.randomUUID().toString();
    return randomUUID + file.getOriginalFilename();
  }

  // s3 이미지를 물리적 삭제하기 위한 메서드
  public void deleteFromS3(String imageUrl) {

    String bucketName = extractBucketName(imageUrl);
    String key = extractKeyFromUrl(imageUrl);

    S3Client.deleteObject(bucketName, key);
  }

  private String extractBucketName(String imageUrl) {
    return bucket;
  }

  private String extractKeyFromUrl(String imageUrl) {
    return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
  }
}
