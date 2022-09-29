package com.codestates.seb006main.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_EXISTS(409, "Member exists"),
    MEMBER_NOT_FOUND(404, "Member not found"),
    POST_NOT_FOUND(404, "Post not found"),
    DISPLAY_NAME_EXISTS(409, "Member exists"),
    MEMBER_NOT_ACTIVE(409, "Member not active"),
    TOKEN_EXPIRED(401, "token is expired"),
    IMAGE_NOT_FOUND(404, "Image not found"),
    PASSWORD_NOT_MATCHED(401, "password not matched"),
    PERMISSION_DENIED(403, "Permission denied."),
    BOOKMARK_NOT_FOUND(404,"Bookmark not found"),
    BLOCK_NOT_FOUND(404, "Block not found");


    @Getter
    private int statusCode;
    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }
}
