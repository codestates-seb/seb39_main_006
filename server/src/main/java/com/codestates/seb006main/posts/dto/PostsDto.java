package com.codestates.seb006main.posts.dto;

import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.util.Period;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostsDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        // TODO: groupDto.Post를 받거나, posts와 group의 postDto를 둘 다 받는 방식.
        private String title;
        private String body;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String startDate;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String endDate;
        // TODO: 지역에 관련된 객체를 만들어 받는 것이 좋다. (확장성 + 세부 지역)
        private String location;
        private Integer totalCount;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String closeDate;
        private List<Long> images;

        @Builder

        public Post(String title, String body, String startDate, String endDate, String location, Integer totalCount, String closeDate, List<Long> images) {
            this.title = title;
            this.body = body;
            this.startDate = startDate;
            this.endDate = endDate;
            this.location = location;
            this.totalCount = totalCount;
            this.closeDate = closeDate;
            this.images = images;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {
        private String title;
        private String body;
        private Integer totalCount;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String closeDate;
        private List<Long> images;

        public Patch(String title, String body, Integer totalCount, String closeDate, List<Long> images) {
            this.title = title;
            this.body = body;
            this.totalCount = totalCount;
            this.closeDate = closeDate;
            this.images = images;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long postId;
        private String title;
        private String body;
        private Long leaderId;
        private String leaderName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String location;
        private Integer totalCount;
        private List<MemberDto.Response> participants;
        private Integer participantsCount;
        private LocalDate closeDate;
        private Posts.PostsStatus postsStatus;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        // TODO: Group의 id를 꺼내줄 것이냐. group 전체를 보여줄 것이냐.
//        private GroupDto.Response group;
        // TODO: 게시글 등록 당시 URL을 넘겨주었으니 필요 없지 않을까?
//        private List<ImageDto.Response> images;

        @Builder
        public Response(Long postId, String title, String body, Member member, Period travelPeriod, String location, Integer totalCount, List<MemberDto.Response> participants, LocalDate closeDate, Posts.PostsStatus postsStatus, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.postId = postId;
            this.title = title;
            this.body = body;
            this.leaderId = member.getMemberId();
            this.leaderName = member.getDisplayName();
            this.startDate = travelPeriod.getStartDate();
            this.endDate = travelPeriod.getEndDate();
            this.location = location;
            this.totalCount = totalCount;
            this.participants = participants;
            this.participantsCount = participants.size();
            this.closeDate = closeDate;
            this.postsStatus = postsStatus;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Group {
        private Long postId;
        private LocalDate startDate;
        private LocalDate endDate;
        private String location;
        private Integer totalCount;
        private List<MemberDto.Response> participantsList;
        private Integer participantsCount;
        private Posts.PostsStatus postsStatus;
        private LocalDate closeDate;

        @Builder
        public Group(Long postId, Period travelPeriod, String location, Integer totalCount, List<MemberDto.Response> participantsList, Integer participantsCount, Posts.PostsStatus postsStatus, LocalDate closeDate) {
            this.postId = postId;
            this.startDate = travelPeriod.getStartDate();
            this.endDate = travelPeriod.getEndDate();
            this.location = location;
            this.totalCount = totalCount;
            this.participantsList = participantsList;
            this.participantsCount = participantsCount;
            this.postsStatus = postsStatus;
            this.closeDate = closeDate;
        }
    }
}
