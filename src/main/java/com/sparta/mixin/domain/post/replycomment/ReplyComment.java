package com.sparta.mixin.domain.post.replycomment;

import com.sparta.mixin.domain.post.comment.entity.PostComment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "reply_comment")
public class ReplyComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String replyComment;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment comment;
}
