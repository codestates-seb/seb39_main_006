package com.codestates.seb006main.Image.repository;

import com.codestates.seb006main.Image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByStoredPath(String storedPath);
}
