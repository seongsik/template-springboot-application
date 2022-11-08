package com.sik.template.biz.api.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.domain.entity.Board;
import static com.sik.template.domain.entity.QBoard.board;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> findAllBoard(Pageable pageable) {
        return jpaQueryFactory.selectFrom(board)
                .leftJoin(board.boardComments)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Board> searchBoard(Pageable pageable, BoardDTO dto) {
        return jpaQueryFactory.selectFrom(board)
                .where(
                        likeTitle(dto.getTitle())
                        , likeContent(dto.getContent())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression likeTitle(String title) {
        if(title == null) return null;
        return board.title.like(title);
    }

    private BooleanExpression likeContent(String content) {
        if(content == null) return null;
        return board.content.like(content);
    }


}
