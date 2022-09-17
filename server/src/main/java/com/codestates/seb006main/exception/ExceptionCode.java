package com.codestates.seb006main.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_EXISTS(409, "Member exists"),
    DISPLAY_NAME_EXISTS(409, "Member exists"),
    EMAIL_EXISTS(409, "Email exists");


    @Getter
    private int statusCode;
    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }
}
