package com.rodiugurlu.junittests.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BOYLE BIR OGRENCI BULUNMAMAKTADIR");
    private final HttpStatus httpStatus;
    private final String errorMessage;


}
