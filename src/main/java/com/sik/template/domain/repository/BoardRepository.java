package com.sik.template.domain.repository;

import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
