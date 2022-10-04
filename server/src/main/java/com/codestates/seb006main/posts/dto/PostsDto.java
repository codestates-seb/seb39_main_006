package com.codestates.seb006main.posts.dto;

import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.util.Period;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostsDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        @NotBlank(message = "제목을 입력해주세요.")
        @Size(min = 5, message = "제목은 5자 이상이어야 합니다.")
        @Size(max = 100, message = "제목은 100자를 넘길 수 없습니다.")
        private String title;
        @NotBlank(message = "내용을 입력해주세요.")
        @Size(min = 10, message = "내용은 10자 이상이어야 합니다.")
        @Size(max = 3000, message = "내용은 3000자를 넘길 수 없습니다.")
        private String body;
        @NotBlank(message = "시작 날짜를 입력해주세요.")
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String startDate;
        @NotBlank(message = "종료 날짜를 입력해주세요.")
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String endDate;
        // TODO: 지역에 관련된 객체를 만들어 받는 것이 좋다. (확장성 + 세부 지역)
        @NotBlank(message = "지역을 입력해주세요.")
        @Size(min = 2, message = "지역은 2자 이상이어야 합니다.")
        @Size(max = 20, message = "지역은 20를 넘길 수 없습니다.")
        private String location;
        @NotNull(message = "모집인원을 입력해주세요.")
        @Positive(message = "양수만 입력할 수 있습니다.")
        @Max(value = 20, message = "모집인원은 20명을 넘길 수 없습니다.")
        private Integer totalCount;
        @NotBlank(message = "매칭 종료 날짜를 입력해주세요.")
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String closeDate;

        @Builder
        public Post(String title, String body, String startDate, String endDate, String location, Integer totalCount, String closeDate) {
            this.title = title;
            this.body = body;
            this.startDate = startDate;
            this.endDate = endDate;
            this.location = location;
            this.totalCount = totalCount;
            this.closeDate = closeDate;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {
        @NotBlank(message = "제목을 입력해주세요.")
        @Size(min = 5, message = "제목은 5자 이상이어야 합니다.")
        @Size(max = 100, message = "제목은 100자를 넘길 수 없습니다.")
        private String title;
        @NotBlank(message = "내용을 입력해주세요.")
        @Size(min = 10, message = "내용은 10자 이상이어야 합니다.")
        @Size(max = 3000, message = "내용은 3000자를 넘길 수 없습니다.")
        private String body;
        @NotNull(message = "모집인원을 입력해주세요.")
        @Positive(message = "양수만 입력할 수 있습니다.")
        @Max(value = 20, message = "모집인원은 20명을 넘길 수 없습니다.")
        private Integer totalCount;
        @NotBlank(message = "매칭 종료 날짜를 입력해주세요.")
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String closeDate;

        @Builder
        public Patch(String title, String body, Integer totalCount, String closeDate) {
            this.title = title;
            this.body = body;
            this.totalCount = totalCount;
            this.closeDate = closeDate;
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
        private List<MemberDto.Participants> participants;
        private Integer participantsCount;
        private LocalDate closeDate;
        private Posts.PostsStatus postsStatus;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        @Builder
        public Response(Long postId, String title, String body, Member member, Period travelPeriod, String location, Integer totalCount, List<MemberDto.Participants> participants, LocalDate closeDate, Posts.PostsStatus postsStatus, LocalDateTime createdAt, LocalDateTime modifiedAt) {
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
