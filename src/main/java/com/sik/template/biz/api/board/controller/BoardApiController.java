package com.sik.template.biz.api.board.controller;

import com.sik.template.biz.api.base.response.RestApiResponse;
import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.biz.api.board.service.BoardApiService;
import com.sik.template.biz.api.board.vo.BoardVO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardApiController {
    private static final Logger logger = LoggerFactory.getLogger(BoardApiController.class);

    BoardApiService boardApiService;

    @GetMapping("/")
    public RestApiResponse<BoardVO, List<BoardDTO>> boards(Pageable pageable, @ModelAttribute BoardVO vo) {
        RestApiResponse<BoardVO, List<BoardDTO>> res = new RestApiResponse<>();

        res.setPageable(pageable);
        res.setParams(vo);
        res.setData(boardApiService.findAllBoard(pageable, vo));

        return res;
    }

    @GetMapping("/{id}")
    public RestApiResponse<BoardVO, BoardDTO>  boards(@PathVariable Long id) {
        RestApiResponse<BoardVO, BoardDTO> res = new RestApiResponse<>();

        BoardVO vo = new BoardVO();
        vo.setSearchId(id);
        res.setParams(vo);
        res.setData(boardApiService.findBoard(vo));

        return res;
    }
}
