package com.sparta.mixin.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Boolean isBookmark;

    public MeetPostResponseDto(MeetPost post) {
        super(post);
        this.bookmarkCount = post.getBookmarkCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.meetId=post.getMeet().getId();
        this.isBookmark = null;
    }

    public MeetPostResponseDto(MeetPost post, List<ImageResponseDto> imageResponseDtos, VoteResponseDto voteResponseDto,boolean isBookmark) {
        super(post,imageResponseDtos,voteResponseDto);
        this.bookmarkCount = post.getBookmarkCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.meetId=post.getMeet().getId();
        this.isBookmark=isBookmark;
    }

    @JsonProperty("isBookmark") // JSON 필드 이름 명시
    public Boolean getIsBookmark() {
        return isBookmark; // null일 경우 응답에서 제외
    }
}
