package com.codestates.seb006main.Image.controller;

import com.codestates.seb006main.Image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        Map<String, Object> response = imageService.uploadImage(multipartFile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{image-id}")
    public ResponseEntity deleteImage(@PathVariable("image-id") Long imageId) {
        imageService.deleteImage(imageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
