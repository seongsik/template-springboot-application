package com.sik.template.biz.api.board.service;

import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.biz.api.board.vo.BoardVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardApiService {

    List<BoardDTO> findAllBoard(Pageable pageable, BoardVO vo);
    BoardDTO findBoard(BoardVO vo);
}
