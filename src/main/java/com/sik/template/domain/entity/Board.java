package com.sik.template.domain.entity;

import com.sik.template.domain.base.BaseAuditTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String content;

    @Column(name = "HIT", nullable = false)
    private int hit;


    @OneToMany(mappedBy = "board", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<BoardComment> boardComments = new ArrayList<>();

}
