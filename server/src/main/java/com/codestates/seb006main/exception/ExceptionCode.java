package com.codestates.seb006main.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_EXISTS(409, "Member exists"),
    GROUP_IS_FULL(409, "Group is full"),
    MEMBER_NOT_FOUND(404, "Member not found"),
    POST_NOT_FOUND(404, "Post not found"),
    PARTICIPANT_NOT_FOUND(404, "Participant not found"),
    DISPLAY_NAME_EXISTS(409, "Member exists"),
    MEMBER_NOT_ACTIVE(409, "Member not active"),
    ALREADY_PARTICIPATED(409, "Already participated"),
    ALREADY_REFUSED(409, "Already refused"),
    ALREADY_ACCEPTED(409, "Already accepted"),
    TOKEN_EXPIRED(401, "token is expired"),
    IMAGE_NOT_FOUND(404, "Image not found"),
    PASSWORD_NOT_MATCHED(401, "password not matched"),
    PERMISSION_DENIED(403, "Permission denied."),
    BOOKMARK_NOT_FOUND(404,"Bookmark not found"),
    BLOCK_NOT_FOUND(404, "Block not found"),
    FEED_NOT_FOUND(404, "Feed not found"),
    COMMENT_NOT_FOUND(404, "Comment not found"),
    MATCHING_NOT_FOUND(404, "Matching not found");



    @Getter
    private int statusCode;
    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }
}
