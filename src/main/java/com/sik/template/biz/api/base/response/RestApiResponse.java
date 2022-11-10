package com.sik.template.biz.api.base.response;

import com.sik.template.biz.api.base.dto.BaseDTO;
import com.sik.template.biz.api.base.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestApiResponse <V extends BaseVO, T> {
    private Pageable pageable;
    private V params;
    private T data;
}
