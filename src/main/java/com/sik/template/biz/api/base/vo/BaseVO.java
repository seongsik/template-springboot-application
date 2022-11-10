package com.sik.template.biz.api.base.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseVO {
    private String searchCondition = null;
    private String searchText = null;

    private Long searchId = null;
    private LocalDateTime searchFromDate = null;
    private LocalDateTime searchToDate = null;
}
