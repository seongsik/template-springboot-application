package com.sik.template.biz.api.board.controller;

import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.biz.api.board.service.BoardApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardApiController {
    private static final Logger logger = LoggerFactory.getLogger(BoardApiController.class);

    BoardApiService boardApiService;

    @GetMapping("/")
    public List<BoardDTO> boards(Pageable pageable) {
        return boardApiService.findAllBoard(pageable);
    }

    @GetMapping("/{id}")
    public List<BoardDTO> boards(@PathVariable Long id) {
        return boardApiService.findBoard(id);
    }
}
