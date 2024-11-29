package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.image.dto.ImageResponseDto;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.vote.dto.VoteResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class MeetPostResponseDto extends PostResponseDto{
    private Long bookmarkCount;
    private Long likeCount;
    private Long commentCount;
    private Long meetId;
    private boolean isBookmark;

    public MeetPostResponseDto(MeetPost post) {
        super(post);
        this.bookmarkCount = post.getBookmarkCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.meetId=post.getMeet().getId();
    }

    public MeetPostResponseDto(MeetPost post, List<ImageResponseDto> imageResponseDtos, VoteResponseDto voteResponseDto,boolean isBookmark) {
        super(post,imageResponseDtos,voteResponseDto);
        this.bookmarkCount = post.getBookmarkCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.meetId=post.getMeet().getId();
        this.isBookmark=isBookmark;
    }
}
