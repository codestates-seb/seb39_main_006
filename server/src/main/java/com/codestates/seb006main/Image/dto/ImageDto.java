package com.codestates.seb006main.Image.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ImageDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long imageId;
        private String originName;
        private String storedPath;

        @Builder
        public Response(Long imageId, String originName, String storedPath) {
            this.imageId = imageId;
            this.originName = originName;
            this.storedPath = storedPath;
        }
    }
}
