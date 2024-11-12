package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.post.entity.PublicPost;
import lombok.Getter;

@Getter
public class PublicPostResponseDto extends PostResponseDto {
    private Long bookmarkCount;
    private Long likeCount;
    private Long commentCount;

    public PublicPostResponseDto(PublicPost post) {
        super(post);
        this.bookmarkCount = post.getBookmarkCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
    }
}
