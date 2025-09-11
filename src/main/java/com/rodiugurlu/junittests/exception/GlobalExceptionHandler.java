package com.rodiugurlu.junittests.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(StudentException.class)
//    public ResponseEntity<ErrorResponse> handleStudentException(StudentException ex) {
//        ErrorCode errorCode = ex.getErrorCode();
//
//        ErrorResponse response = new ErrorResponse(
//                errorCode.name(),
//                errorCode.getErrorMessage(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(response, errorCode.getHttpStatus());
//
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
