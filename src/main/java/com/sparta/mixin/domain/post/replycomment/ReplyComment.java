package com.sparta.mixin.domain.post.replycomment;

import com.sparta.mixin.domain.post.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.post.comment.entity.PostComment;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "reply_comment")
@RequiredArgsConstructor
public class ReplyComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String replyComment;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ReplyComment(PostComment postComment, CommentRequestDto commentRequestDto, User loginUser) {
        this.postComment=postComment;
        this.replyComment=commentRequestDto.getComment();
        this.user=loginUser;
    }

    public void updateReplyComment(CommentRequestDto commentRequestDto) {
        this.replyComment=commentRequestDto.getComment();
    }
}
