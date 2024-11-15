package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.post.entity.MeetNotice;
import lombok.Getter;

@Getter
public class MeetNoticeResponseDto extends PostResponseDto{
    private Long meetId;
    private boolean isRead;

    public MeetNoticeResponseDto(MeetNotice post, boolean isRead) {
        super(post);
        this.meetId = post.getMeet().getId();
        this.isRead = isRead;
    }
}
