package com.codestates.seb006main.group.entity;

import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberGroupId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public MemberGroup(Long memberGroupId, Member member, Group group) {
        this.memberGroupId = memberGroupId;
        this.member = member;
        this.group = group;
    }
}
