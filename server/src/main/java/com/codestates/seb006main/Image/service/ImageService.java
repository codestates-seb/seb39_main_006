package com.codestates.seb006main.Image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.Image.repository.ImageRepository;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.util.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final FileHandler fileHandler;
    final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;
    @Value("${cloud.aws.s3.alter-domain}")
    private String domain;

    public Map<String, Object> uploadImage(MultipartFile image) throws IOException {
        String originName = image.getOriginalFilename();
        String storedName = fileHandler.storeFile(image);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, storedName, image.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

//        String imagePath = amazonS3Client.getUrl(S3Bucket, storedName).toString();
//        String imagePath = "https://" + S3Bucket + "/" + storedName;
        String imagePath = "https://" + domain + "/" + storedName;

        Image saveImage = imageRepository.save(Image.builder()
                .originName(originName)
                .storedName(storedName)
                .storedPath(imagePath)
                .fileSize(image.getSize())
                .uploadedAt(LocalDateTime.now())
                .build());

        return new HashMap<String, Object>() {{
            put("imageId" , saveImage.getImageId());
            put("imageUrl", imagePath);
        }};
    }

    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
        amazonS3Client.deleteObject(S3Bucket, image.getStoredPath().substring(image.getStoredPath().lastIndexOf("/")+1));
        imageRepository.deleteById(imageId);
    }
}
