package com.sik.template.biz.api.board.controller;

import com.sik.template.biz.api.base.response.RestApiResponse;
import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.biz.api.board.service.BoardApiService;
import com.sik.template.biz.api.board.vo.BoardVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardApiController {
    BoardApiService boardApiService;

    @Operation(
            summary = "게시글 목록"
            , description = "게시글 목록을 조회한다."
    )
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestApiResponse<BoardVO, List<BoardDTO>> findAllBoard(Pageable pageable, @ModelAttribute BoardVO vo) {
        RestApiResponse<BoardVO, List<BoardDTO>> res = new RestApiResponse<>();

        res.setPageable(pageable);
        res.setParams(vo);
        res.setData(boardApiService.findAllBoard(pageable, vo));

        return res;
    }

    @Operation(
            summary = "게시글 조회"
            , description = "단일 게시글 정보를 조회한다."
    )
    @GetMapping(value = "/{searchId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestApiResponse<BoardVO, BoardDTO> findBoard(@PathVariable Long searchId) {
        RestApiResponse<BoardVO, BoardDTO> res = new RestApiResponse<>();

        BoardVO vo = new BoardVO();
        vo.setSearchId(searchId);
        res.setParams(vo);
        res.setData(boardApiService.findBoard(vo));

        return res;
    }
}
