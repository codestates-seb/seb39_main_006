package com.codestates.seb006main.group.entity;

import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.util.Period;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "GROUPS")
@Entity
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    // TODO: Embedded 타입으로 바꾸는 것도 좋은 선택이지 않을까
//    private LocalDate startDate;
//    private LocalDate endDate;
    @Embedded
    private Period travelPeriod;
    private String location;
    // TODO: 연관관계 매핑된 멤버의 수를 headcount에서 빼면 현재 인원 수가 나온다. -> response로 전달.
    private Integer headcount;
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;
    private LocalDate closeDate;
    @OneToOne(mappedBy = "group")
    private Posts posts;
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<MemberGroup> memberGroups;

    // TODO: 모집 완료된 시간, 혹은 닫힌 시간에 대한 필드도 필요할까?

    @Builder
    public Group(Long groupId, Period travelPeriod, String location, Integer headcount, GroupStatus groupStatus, LocalDate closeDate, Posts posts, List<MemberGroup> memberGroups) {
        this.groupId = groupId;
        this.travelPeriod = travelPeriod;
//        this.startDate = startDate;
//        this.endDate = endDate;
        this.location = location;
        this.headcount = headcount;
        this.groupStatus = Objects.requireNonNullElse(groupStatus, GroupStatus.READY);
        this.closeDate = closeDate;
        this.posts = posts;
        this.memberGroups = memberGroups;
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

    // 비즈니스 로직
    public void checkStatus() {
    }

    public void updateHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    public void updateCloseDate(String closeDate) {
        this.closeDate = LocalDate.parse(closeDate);
    }
}
