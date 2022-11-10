package com.sik.template.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@Getter
public class ApiBizException extends RuntimeException {
    private ExceptionCode exceptionCode;
    private String message;


    public ApiBizException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public ApiBizException(ExceptionCode exceptionCode, String message) {
        this.exceptionCode = exceptionCode;
        this.message = message;
    }
}
