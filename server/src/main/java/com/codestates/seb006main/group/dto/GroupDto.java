package com.codestates.seb006main.group.dto;

import com.codestates.seb006main.group.entity.Group;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GroupDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post{
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private LocalDate startDate;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private LocalDate endDate;
        // TODO: 지역에 관련된 객체를 만들어 받는 것이 좋다. (확장성 + 세부 지역)
        private String location;
        private Integer headcount;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private LocalDate closeDate;

        @Builder
        public Post(LocalDate startDate, LocalDate endDate, String location, Integer headcount, LocalDate closeDate) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.location = location;
            this.headcount = headcount;
            this.closeDate = closeDate;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch{
        // TODO: 무엇을 수정할 것인가? 어떤 요소를 수정 가능하게 할 것인가?
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private Long groupId;
        private LocalDate startDate;
        private LocalDate endDate;
        private String location;
        private Integer headcount;
//        private Integer currentMembers;
//        private List<MemberDto.Response> currentMemberList;
        private Group.GroupStatus groupStatus;
        private LocalDate closeDate;

        @Builder
        public Response(Long groupId, LocalDate startDate, LocalDate endDate, String location, Integer headcount, Group.GroupStatus groupStatus, LocalDate closeDate) {
            this.groupId = groupId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.location = location;
            this.headcount = headcount;
            this.groupStatus = groupStatus;
            this.closeDate = closeDate;
        }
    }
}
