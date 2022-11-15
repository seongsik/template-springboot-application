package com.sik.template.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sik.template.biz.api.board.vo.BoardVO;
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
    public List<Board> findAllBoard(Pageable pageable, BoardVO vo) {
        return jpaQueryFactory.selectFrom(board)
                .where(
                        likeTitle(vo.getSearchText())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression likeTitle(String title) {
        if(title == null) return null;
        return board.title.contains(title);
    }

}
