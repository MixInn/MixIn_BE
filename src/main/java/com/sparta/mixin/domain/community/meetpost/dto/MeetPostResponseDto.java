package com.sparta.mixin.domain.community.meetpost.dto;

import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import lombok.Getter;

@Getter
public class MeetPostResponseDto {
    private Long id;
    private Long meetId;
    private String title;
    private String content;

    public MeetPostResponseDto(MeetPost meetPost) {
        this.id=meetPost.getId();
        this.meetId=meetPost.getMeet().getId();
        this.title= meetPost.getTitle();
        this.content= meetPost.getContent();
    }
}
