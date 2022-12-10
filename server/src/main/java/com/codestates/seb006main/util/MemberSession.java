package com.codestates.seb006main.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberSession {
    private Long memberId;
    private List<String> sessionIds = new ArrayList<>();

    public MemberSession(Long memberId, String sessionId) {
        this.memberId = memberId;
        this.sessionIds.add(sessionId);
    }
    public MemberSession() {

    }
}
