package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.image.dto.ImageResponseDto;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.post.vote.dto.VoteResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class MeetNoticeResponseDto extends PostResponseDto{
    private Long meetId;
    private boolean isRead;

    public MeetNoticeResponseDto(MeetNotice post, boolean isRead, List<ImageResponseDto> imageResponseDtos,
        VoteResponseDto voteResponseDto) {
        super(post,imageResponseDtos,voteResponseDto);
        this.meetId = post.getMeet().getId();
        this.isRead = isRead;
    }
}
