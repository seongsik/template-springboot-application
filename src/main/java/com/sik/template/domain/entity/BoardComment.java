package com.sik.template.domain.entity;

import com.sik.template.domain.base.BaseAuditTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String content;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;



}
