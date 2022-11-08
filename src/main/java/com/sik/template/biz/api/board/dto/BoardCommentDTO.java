package com.sik.template.biz.api.board.dto;

import com.sik.template.biz.api.base.dto.BaseDTO;
import lombok.Data;

@Data
public class BoardCommentDTO extends BaseDTO {
    private Long boardCommentId;
    private Long boardId;

    private String content;
}
