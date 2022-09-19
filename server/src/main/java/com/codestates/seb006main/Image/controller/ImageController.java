package com.codestates.seb006main.Image.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @PostMapping
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        Map<String, String> response = imageService.createImage();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
