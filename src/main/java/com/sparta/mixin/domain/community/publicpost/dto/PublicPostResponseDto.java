package com.sparta.mixin.domain.community.publicpost.dto;

import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PublicPostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PublicPostResponseDto(PublicPost publicPost) {
        this.id= publicPost.getId();
        this.title= publicPost.getTitle();
        this.content= publicPost.getContent();
        this.userId=publicPost.getUser().getId();
        this.createdAt=publicPost.getCreatedAt();
        this.modifiedAt=publicPost.getModifiedAt();
    }
}
