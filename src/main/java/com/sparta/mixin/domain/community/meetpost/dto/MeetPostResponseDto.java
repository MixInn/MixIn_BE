package com.sparta.mixin.domain.community.meetpost.dto;

import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MeetPostResponseDto {
    private Long id;
    private Long meetId;
    private String title;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MeetPostResponseDto(MeetPost meetPost) {
        this.id=meetPost.getId();
        this.meetId=meetPost.getMeet().getId();
        this.title= meetPost.getTitle();
        this.content= meetPost.getContent();
        this.userId=meetPost.getUser().getId();
        this.createdAt=meetPost.getCreatedAt();
        this.modifiedAt=meetPost.getModifiedAt();
    }
}
