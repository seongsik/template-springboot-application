package com.sik.template.domain.entity;

import com.sik.template.domain.base.BaseAuditTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "BOARD_COMMENT")
public class BoardComment extends BaseAuditTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_COMMENT_ID")
    private Long boardCommentId;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "CONTENT")
    private byte content;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Builder
    public BoardComment(String title, byte content) {
        this.title = title;
        this.content = content;
    }

}
