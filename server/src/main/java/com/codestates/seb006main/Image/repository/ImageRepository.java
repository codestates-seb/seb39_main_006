package com.codestates.seb006main.Image.repository;

import com.codestates.seb006main.Image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
    Optional<Image> findByStoredPath(String storedPath);
    List<Image> findByUploadedAtBefore(LocalDateTime uploadedAt);
}
