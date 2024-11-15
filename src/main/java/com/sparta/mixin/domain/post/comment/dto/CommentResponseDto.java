package com.sparta.mixin.domain.post.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.mixin.domain.post.PostType;
import com.sparta.mixin.domain.post.comment.entity.PostComment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {

    private Long id;
    private Long postId;
    private PostType postType;
    private String comment;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ReplyCommentResponseDto> replyCommentResponseDtos;

    public CommentResponseDto(PostComment postComment,List<ReplyCommentResponseDto> replyCommentResponseDtos) {
        this.id = postComment.getId();
        this.comment = postComment.getComment();
        this.userId = postComment.getUser().getId();
        this.postType = postComment.getPostType();
        this.postId = postComment.getPost().getId();
        this.createdAt = postComment.getCreatedAt();
        this.modifiedAt = postComment.getModifiedAt();
        this.replyCommentResponseDtos=replyCommentResponseDtos;
    }
    public CommentResponseDto(PostComment postComment) {
        this.id = postComment.getId();
        this.comment = postComment.getComment();
        this.userId = postComment.getUser().getId();
        this.postType = postComment.getPostType();
        this.postId = postComment.getPost().getId();
        this.createdAt = postComment.getCreatedAt();
        this.modifiedAt = postComment.getModifiedAt();
    }
}
