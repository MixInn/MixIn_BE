package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.image.dto.ImageResponseDto;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.post.vote.dto.VoteResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class PublicPostResponseDto extends PostResponseDto {
    private Long bookmarkCount;
    private Long likeCount;
    private Long commentCount;
    private boolean isBookmark;

    public PublicPostResponseDto(PublicPost post) {
        super(post);
        this.bookmarkCount = post.getBookmarkCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
    }

    public PublicPostResponseDto(PublicPost post, List<ImageResponseDto> imageResponseDtos, VoteResponseDto voteResponseDto,boolean isBookmark) {
        super(post,imageResponseDtos,voteResponseDto);
        this.bookmarkCount = post.getBookmarkCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.isBookmark=isBookmark;
    }
}
