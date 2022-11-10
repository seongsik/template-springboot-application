package com.sik.template.biz.api.base.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDTO implements Serializable {
    private String createdBy;
    private String createdDate;

    private String lastModifiedBy;
    private String lastModifiedDate;

}
