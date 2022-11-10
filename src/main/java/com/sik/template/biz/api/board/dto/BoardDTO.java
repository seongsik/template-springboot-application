package com.sik.template.biz.api.board.dto;

import com.sik.template.biz.api.base.dto.BaseDTO;
import com.sik.template.domain.entity.Board;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardDTO extends BaseDTO {
    private Long boardId;
    private String title;
    private String content;

    private List<BoardCommentDTO> boardComments;


    public static BoardDTO convert(Board entity) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(entity, BoardDTO.class);
    }
    public static List<BoardDTO> convertList(List<Board> entities) {
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<Board, BoardDTO>() {
            @Override
            protected void configure() {
                // Lazy Loading N+1 회피를 위한 제외
                skip(destination.getBoardComments());
            }
        });

        return entities.stream().map(o -> mapper.map(o, BoardDTO.class)).collect(Collectors.toList());
    }
}
