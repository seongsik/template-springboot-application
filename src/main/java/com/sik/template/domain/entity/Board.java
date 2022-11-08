package com.sik.template.domain.entity;

import com.sik.template.domain.base.BaseAuditTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "BOARD")
public class Board extends BaseAuditTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "TITLE")
    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "CONTENT")
    private byte content;

    @Column(name = "HIT", nullable = false)
    private int hit;

    @Builder
    public Board(String title, byte content) {
        this.title = title;
        this.content = content;
    }

    @OneToMany(mappedBy = "board", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<BoardComment> boardComments = new ArrayList<>();

}
