package com.sik.template.biz.api.account.dto;

import lombok.Data;

@Data
public class SignDTO {
    private String result;
    private String message;
    private String token;
}
