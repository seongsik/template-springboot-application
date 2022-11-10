package com.sik.template.biz.api.base.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseDTO implements Serializable {
    private String createdBy;
    private LocalDateTime createdDate;

    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
