package com.sik.template.biz.api.base.response;

import com.sik.template.biz.api.base.dto.BaseDTO;
import com.sik.template.biz.api.base.vo.BaseVO;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class RestApiResponse <V extends BaseVO, T> {
    private int code;
    private String message;

    private Pageable pageable;
    private V params;
    private T data;
}
