package com.sik.template.common.advice;

import com.sik.template.biz.api.base.response.RestApiExceptionResponse;
import com.sik.template.biz.api.base.response.RestApiResponse;
import com.sik.template.biz.api.base.vo.BaseVO;
import com.sik.template.common.exception.ApiBizException;
import com.sik.template.common.exception.ExceptionCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({ApiBizException.class})
    public ResponseEntity<RestApiExceptionResponse> exeptionHandler(HttpServletRequest request, final ApiBizException e) {
        return ResponseEntity
                .status(e.getExceptionCode().getStatus())
                .body(
                        RestApiExceptionResponse.builder()
                                .errorCode(e.getExceptionCode().getCode())
                                .errorMessage(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<RestApiExceptionResponse> exeptionHandler(HttpServletRequest request, final RuntimeException e) {
        return ResponseEntity
                .status(ExceptionCode.RUNTIME_EXCEPTION.getStatus())
                .body(
                        RestApiExceptionResponse.builder()
                                .errorCode(ExceptionCode.RUNTIME_EXCEPTION.getCode())
                                .errorMessage(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<RestApiExceptionResponse> exeptionHandler(HttpServletRequest request, final AccessDeniedException e) {
        return ResponseEntity
                .status(ExceptionCode.UNAUTHORIZED.getStatus())
                .body(
                        RestApiExceptionResponse.builder()
                                .errorCode(ExceptionCode.UNAUTHORIZED.getCode())
                                .errorMessage(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<RestApiExceptionResponse> exeptionHandler(HttpServletRequest request, final Exception e) {
        return ResponseEntity
                .status(ExceptionCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(
                        RestApiExceptionResponse.builder()
                                .errorCode(ExceptionCode.INTERNAL_SERVER_ERROR.getCode())
                                .errorMessage(e.getMessage())
                                .build()
                );
    }
}
