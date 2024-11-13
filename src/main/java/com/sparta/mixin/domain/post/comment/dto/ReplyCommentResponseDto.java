package com.sparta.mixin.domain.post.comment.dto;

import com.sparta.mixin.domain.post.replycomment.ReplyComment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyCommentResponseDto {
    private Long id;
    private String replyComment;
    private Long postCommentId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReplyCommentResponseDto(ReplyComment replyComment) {
        this.id= replyComment.getId();
        this.replyComment=replyComment.getReplyComment();
        this.postCommentId=replyComment.getPostComment().getId();
        this.userId=replyComment.getUser().getId();
        this.createdAt=replyComment.getCreatedAt();
        this.modifiedAt=replyComment.getModifiedAt();
    }
}
