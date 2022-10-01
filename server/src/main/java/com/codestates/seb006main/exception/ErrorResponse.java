package com.codestates.seb006main.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String engMessage;
    private String korMessage;
//    private String fieldErrors;
//    private String violationErrors;

    public ErrorResponse(BusinessLogicException e) {
        this.status = e.getExceptionCode().getStatusCode();
        this.engMessage = e.getMessage();
        this.korMessage = e.getKorMessage();
    }
}
