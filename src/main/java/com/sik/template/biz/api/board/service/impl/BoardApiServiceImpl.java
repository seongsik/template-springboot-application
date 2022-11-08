package com.sik.template.biz.api.board.service.impl;

import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.biz.api.board.repository.BoardCustomRepository;
import com.sik.template.biz.api.board.service.BoardApiService;
import com.sik.template.domain.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BoardApiServiceImpl implements BoardApiService {

    BoardRepository boardRepository;
    BoardCustomRepository boardCustomRepository;



    @Override
    public List<BoardDTO> findAllBoard(Pageable pageable) {
        return boardCustomRepository.findAllBoard(pageable);
    }

    @Override
    public List<BoardDTO> findBoard(Long id) {
        return null;
    }
}
