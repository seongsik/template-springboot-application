package com.sik.template.biz.api.base.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RestApiExceptionResponse {
    private String errorCode;
    private String errorMessage;
}
