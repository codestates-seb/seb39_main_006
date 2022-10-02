package com.codestates.seb006main.exception;

import lombok.Getter;

public class BusinessLogicException extends RuntimeException{

    @Getter
    private ExceptionCode exceptionCode;
    @Getter
    private String korMessage;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getEngMessage());
        this.korMessage = exceptionCode.getKorMessage();
        this.exceptionCode = exceptionCode;
    }
}
