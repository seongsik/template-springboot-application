package com.sik.template.biz.api.board.dto;

import com.sik.template.biz.api.base.dto.BaseDTO;
import lombok.Data;

import java.util.List;

@Data
public class BoardDTO extends BaseDTO {
    private int boardId;
    private String title;
    private String content;

    private List<BoardCommentDTO> boardComments;
}
