package com.codestates.seb006main.members.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class BookmarkDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private Long postId;

        @Builder
        public Response(Long postId) {
            this.postId = postId;
        }
    }




}
