package com.sparta.mixin.domain.community.comment.dto;

import com.sparta.mixin.domain.community.CommunityType;
import com.sparta.mixin.domain.community.comment.entity.CommunityComment;
import com.sparta.mixin.global.Timestamped;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private CommunityType communityType;
    private String comment;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(CommunityComment communityComment) {
        this.id= communityComment.getId();
        this.comment= communityComment.getComment();
        this.userId=communityComment.getUser().getId();
        this.communityType=communityComment.getCommunityType();

        if (communityComment.getCommunityType() == CommunityType.MEETPOST) {
            this.postId = communityComment.getMeetPost().getId();
        } else if (communityComment.getCommunityType() == CommunityType.PUBLICPOST) {
            this.postId = communityComment.getPublicPost().getId();
        }
        this.createdAt=communityComment.getCreatedAt();
        this.modifiedAt=communityComment.getModifiedAt();
    }
}
