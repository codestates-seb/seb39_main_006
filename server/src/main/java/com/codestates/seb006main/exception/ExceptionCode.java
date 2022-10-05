package com.codestates.seb006main.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_EXISTS(409, "Member exists", "이미 존재하는 회원입니다."),
    GROUP_IS_FULL(409, "Group is full", "해당 그룹은 이미 가득 찼습니다."),
    MEMBER_NOT_FOUND(404, "Member not found", "존재하지 않는 회원입니다."),
    POST_NOT_FOUND(404, "Post not found", "존재하지 않는 게시글입니다."),
    PARTICIPANT_NOT_FOUND(404, "Participant not found", "존재하지 않는 참여 인원입니다."),
    DISPLAY_NAME_EXISTS(409, "Member exists", "이미 존재하는 닉네임입니다."),
    MEMBER_NOT_ACTIVE(409, "Member not active", "비활성화된 회원입니다."),
    ALREADY_PARTICIPATED(409, "Already participated", "이미 참여하고 있는 그룹입니다."),
    ALREADY_REQUESTED(409, "Already requested", "이미 요청된 매칭입니다. 잠시 후에 다시 시도해주시기 바랍니다."),
    ALREADY_USED_IMAGE(409, "Image already used.", "다른 곳에서 사용된 사진입니다. 새 사진을 업로드 해주세요."),
    ALREADY_REFUSED(409, "Already refused", "이미 거절된 요청입니다."),
    ALREADY_ACCEPTED(409, "Already accepted", "이미 수락된 요정입니다."),
    LACK_OF_INFORMATION(409, "Lack of information", "메시지 정보가 부족합니다. 관리자에게 문의해주세요."),
    TOKEN_EXPIRED(401, "token is expired", "만료된 토큰입니다. 다시 로그인 해주세요."),
    IMAGE_NOT_FOUND(404, "Image not found", "이미지를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCHED(401, "password not matched", "비밀번호가 맞지 않습니다."),
    PERMISSION_DENIED(403, "Permission denied.", "해당 요청을 수행할 권한이 없습니다."),
    BOOKMARK_NOT_FOUND(404,"Bookmark not found", "북마크 정보를 찾을 수 없습니다."),
    BLOCK_NOT_FOUND(404, "Block not found", "차단 정보를 찾을 수 없습니다."),
    FEED_NOT_FOUND(404, "Feed not found", "존재하지 않는 피드입니다."),
    COMMENT_NOT_FOUND(404, "Comment not found", "존재하지 않는 댓글입니다."),
    MATCHING_NOT_FOUND(404, "Matching not found", "매칭 정보를 찾을 수 없습니다."),
    SESSION_NOT_FOUND(404, "Session not found", "세션 정보를 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND(404, "Session not found", "메시지를 찾을 수 없습니다."),
    NOT_ALLOWED_FILENAME_EXTENSION(415, "Not Allowed Filename Extension", "허용되지 않은 파일 형식입니다. PNG, JPG, JPEG, GIF, BMP 확장자만 업로드할 수 있습니다."),
    CLOSE_DATE_VIOLATION(409, "Close Date Violation", "매칭 종료 날짜는 여행 날짜보다 이전이어야 합니다."),
    TRAVEL_DATE_VIOLATION(409, "Travel Date Violation", "여행 시작 날짜는 여행 종료 날짜보다 이전이어야 합니다."),
    DATE_VIOLATION(409, "Date Violation", "현재 날짜보다 이전일 수 없습니다.");



    @Getter
    private int statusCode;
    @Getter
    private String engMessage;
    @Getter
    private String korMessage;

    ExceptionCode(int statusCode, String engMessage, String korMessage) {
        this.statusCode = statusCode;
        this.engMessage = engMessage;
        this.korMessage = korMessage;
    }
}
