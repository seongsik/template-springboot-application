package com.sik.template.biz.api.board.repository;

import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.biz.api.board.vo.BoardVO;
import com.sik.template.domain.entity.Board;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCustomRepository {
    List<Board> findAllBoard(Pageable pageable, BoardVO vo);

}
