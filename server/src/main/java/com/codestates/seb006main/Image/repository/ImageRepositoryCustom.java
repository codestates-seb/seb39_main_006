package com.codestates.seb006main.Image.repository;

import com.codestates.seb006main.Image.entity.Image;

import java.time.LocalDateTime;
import java.util.List;

public interface ImageRepositoryCustom {
    List<Image> findUnusedImages(LocalDateTime aHourAgo);
}
