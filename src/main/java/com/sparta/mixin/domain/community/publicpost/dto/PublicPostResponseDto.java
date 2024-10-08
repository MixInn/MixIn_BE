package com.sparta.mixin.domain.community.publicpost.dto;

import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import lombok.Getter;

@Getter
public class PublicPostResponseDto {
    private Long id;
    private String title;
    private String content;

    public PublicPostResponseDto(PublicPost publicPost) {
        this.id= publicPost.getId();
        this.title= publicPost.getTitle();
        this.content= publicPost.getContent();
    }
}
