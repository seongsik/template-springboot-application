package com.sik.template.biz.api.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class BaseVO {
    @Schema(description = "searchCondition", type = "String")
    private String searchCondition;

    @Schema(description = "searchText", type = "String")
    private String searchText;

    @Schema(description = "searchId", type = "String", example = "1")
    private Long searchId;

    @Schema(description = "searchFromDate", type = "LocalDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchFromDate;

    @Schema(description = "searchToDate", type = "LocalDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchToDate;
}
