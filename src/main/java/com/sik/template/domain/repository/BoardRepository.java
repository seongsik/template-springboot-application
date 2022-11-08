package com.sik.template.domain.repository;

import com.sik.template.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select a from Board a where a.title like %:title%")
    List<Board> findByTitle(String title);
}
