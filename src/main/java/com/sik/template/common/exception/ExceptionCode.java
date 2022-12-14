package com.sik.template.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E0002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "E0004");

    private final HttpStatus status;
    private final String code;

    ExceptionCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }
}
