package com.sparta.mixin.domain.community.publicpost.entity;

import com.sparta.mixin.domain.community.publicpost.dto.PublicPostRequestDto;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "PublicPost")
@RequiredArgsConstructor
public class PublicPost extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Builder
    public PublicPost(PublicPostRequestDto publicPostRequestDto) {
        this.title=publicPostRequestDto.getTitle();
        this.content= publicPostRequestDto.getContent();
    }

    public void updatePost(PublicPostRequestDto publicPostRequestDto) {
        this.title= publicPostRequestDto.getTitle();
        this.content= publicPostRequestDto.getContent();
    }
}
