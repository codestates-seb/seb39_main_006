package com.codestates.seb006main.members.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private Long blockedMemberId;

    @Builder
    public Block(Long blockId, Member member, Long blockedMemberId) {
        this.blockId = blockId;
        this.member = member;
        this.blockedMemberId = blockedMemberId;
    }
}
