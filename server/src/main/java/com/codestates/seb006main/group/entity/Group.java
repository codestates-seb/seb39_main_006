package com.codestates.seb006main.group.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    // TODO: 연관관계 매핑된 멤버의 수를 headcount에서 빼면 현재 인원 수가 나온다. -> response로 전달.
    private Integer headcount;
    private GroupStatus groupStatus;
    private LocalDateTime closeDate;

    // TODO: 모집 완료된 시간, 혹은 닫힌 시간에 대한 필드도 필요할까?

    @Builder
    public Group(Long matchingId, LocalDateTime startDate, LocalDateTime endDate, String location, Integer headcount, GroupStatus groupStatus, LocalDateTime closeDate) {
        this.matchingId = matchingId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.headcount = headcount;
        this.groupStatus = groupStatus;
        this.closeDate = closeDate;
    }

    public enum GroupStatus {
        READY(0, "모집 예정"),
        RECRUITING(1, "모집 중"),
        COMPLETED(2, "모집 완료");

        int stepNumber;
        String groupDescription;

        GroupStatus(int stepNumber, String groupDescription) {
            this.stepNumber = stepNumber;
            this.groupDescription = groupDescription;
        }
    }
}
