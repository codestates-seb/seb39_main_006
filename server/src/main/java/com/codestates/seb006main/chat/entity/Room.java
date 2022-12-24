package com.codestates.seb006main.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Room {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID roomId;
    private String name;
    private Long memberId;
    private Long otherId;
    //    @OneToMany
    //    private List<Chat> chatList;

    @Builder
    public Room(UUID roomId, String name, Long memberId, Long otherId) {
        this.roomId = roomId;
        this.name = name;
        this.memberId = memberId;
        this.otherId = otherId;
    }
}
