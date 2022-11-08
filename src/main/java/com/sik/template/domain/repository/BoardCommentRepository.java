package com.sik.template.domain.repository;

import com.sik.template.domain.entity.Board;
import com.sik.template.domain.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findByBoard(Board board);
}
