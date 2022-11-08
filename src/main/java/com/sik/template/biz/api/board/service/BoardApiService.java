package com.sik.template.biz.api.board.service;

import com.sik.template.biz.api.board.dto.BoardDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardApiService {

    List<BoardDTO> findAllBoard(Pageable pageable);

    List<BoardDTO> findBoard(Long id);
}
