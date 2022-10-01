package com.codestates.seb006main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    // TODO: AOP 구현
    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        ErrorResponse errorResponse = new ErrorResponse(e);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(e.getExceptionCode().getStatusCode()));
    }
}
