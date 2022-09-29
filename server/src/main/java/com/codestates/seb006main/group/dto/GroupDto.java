package com.codestates.seb006main.group.dto;

import com.codestates.seb006main.group.entity.Group;
import com.codestates.seb006main.util.Period;
import com.sun.xml.bind.v2.TODO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class GroupDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post{
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String startDate;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String endDate;
        // TODO: 지역에 관련된 객체를 만들어 받는 것이 좋다. (확장성 + 세부 지역)
        private String location;
        private Integer headcount;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String closeDate;

        @Builder
        public Post(String startDate, String endDate, String location, Integer headcount, String closeDate) {
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
        /* TODO: 무엇을 수정할 것인가? 어떤 요소를 수정 가능하게 할 것인가?
             - 여행 일정과 장소는 낙장불입이라는 의견.
             - 전부 바꿀 수 있어야 한다는 의견.
             => 일단은 모집 종료 날짜와 모집 인원만 수정할 수 있도록 함.
         */
//        @DateTimeFormat(pattern = "YYYY-MM-DD")
//        private String startDate;
//        @DateTimeFormat(pattern = "YYYY-MM-DD")
//        private String endDate;
//        // TODO: 지역에 관련된 객체를 만들어 받는 것이 좋다. (확장성 + 세부 지역)
//        private String location;
        private Integer headcount;
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        private String closeDate;

        @Builder
        public Patch(Integer headcount, String closeDate) {
            this.headcount = headcount;
            this.closeDate = closeDate;
        }
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
        public Response(Long groupId, Period travelPeriod, String location, Integer headcount, Group.GroupStatus groupStatus, LocalDate closeDate) {
            this.groupId = groupId;
            this.startDate = travelPeriod.getStartDate();
            this.endDate = travelPeriod.getEndDate();
            this.location = location;
            this.headcount = headcount;
            this.groupStatus = groupStatus;
            this.closeDate = closeDate;
        }
    }
}