package com.codestates.seb006main.Image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.Image.repository.ImageRepository;
import com.codestates.seb006main.util.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final FileHandler fileHandler;
    final AmazonS3Client amazonS3Client;
    private final String S3Bucket = "seb-main-006";

    public Map<String, String> uploadImage(MultipartFile image) throws IOException {
        String originName = image.getOriginalFilename();
        String storedName = fileHandler.storeFile(image);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, storedName, image.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String imagePath = amazonS3Client.getUrl(S3Bucket, storedName).toString();

        Image saveImage = Image.builder()
                .originName(originName)
                .storedName(storedName)
                .storedPath(imagePath)
                .fileSize(image.getSize())
                .build();

//        image.setPosts(posts);
        imageRepository.save(saveImage);
//        posts.addImage(image);

        return new HashMap<String, String>() {{
            put("Url", imagePath);
        }};
    }

    public void saveImage(MultipartFile image) throws IOException {
        String originName = image.getOriginalFilename();
        String storedName = fileHandler.storeFile(image);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, storedName, image.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String imagePath = amazonS3Client.getUrl(S3Bucket, storedName).toString();

        Image newImage = Image.builder()
                .originName(originName)
                .storedName(storedName)
                .storedPath(imagePath)
                .fileSize(image.getSize())
                .build();

//            image.setPosts(posts);
        imageRepository.save(newImage);
//            posts.addImage(image);
    }

    public void saveImages(List<MultipartFile> images) throws IOException {
        for (MultipartFile img : images) {
            String originName = img.getOriginalFilename();
            String storedName = fileHandler.storeFile(img);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(img.getContentType());
            objectMetadata.setContentLength(img.getSize());

            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, storedName, img.getInputStream(), objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String imagePath = amazonS3Client.getUrl(S3Bucket, storedName).toString();

            Image image = Image.builder()
                    .originName(originName)
                    .storedName(storedName)
                    .storedPath(imagePath)
                    .fileSize(img.getSize())
                    .build();

//            image.setPosts(posts);
            imageRepository.save(image);
//            posts.addImage(image);
        }
    }
}