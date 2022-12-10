package com.codestates.seb006main.chat.dto;

import lombok.*;

import java.util.List;

public class RoomDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post{
        private Long memberId;
        private Long otherId;

        @Builder
        public Post(Long memberId, Long otherId) {
            this.memberId = memberId;
            this.otherId = otherId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private String roomId;
        private String name;
        private Long memberId;
        private Long otherId;

        @Builder
        public Response(String roomId, String name, Long memberId, Long otherId) {
            this.roomId = roomId;
            this.name = name;
            this.memberId = memberId;
            this.otherId = otherId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ResponseList {
        private Long memberId;
        private List<Response> sentRoomList;
        private List<Response> receivedRoomList;

        @Builder
        public ResponseList(Long memberId, List<Response> sentRoomList, List<Response> receivedRoomList) {
            this.memberId = memberId;
            this.sentRoomList = sentRoomList;
            this.receivedRoomList = receivedRoomList;
        }
    }
}
