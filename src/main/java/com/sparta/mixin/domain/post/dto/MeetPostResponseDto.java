package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.post.entity.MeetPost;
import lombok.Getter;

@Getter
public class MeetPostResponseDto extends PostResponseDto{
    private Long bookmarkCount;
    private Long likeCount;
    private Long commentCount;
    private Long meetId;

    public MeetPostResponseDto(MeetPost post) {
        super(post);
        this.bookmarkCount = post.getBookmarkCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.meetId=post.getMeet().getId();
    }
}
