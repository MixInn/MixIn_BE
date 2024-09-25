package com.sparta.mixin.domain.community.comment.dto;

import com.sparta.mixin.domain.community.comment.entity.CommunityComment;

public class CommentResponseDto {
    private Long id;
    private String comment;

    public CommentResponseDto(CommunityComment communityComment) {
        this.id= communityComment.getId();
        this.comment= communityComment.getComment();
    }
}
