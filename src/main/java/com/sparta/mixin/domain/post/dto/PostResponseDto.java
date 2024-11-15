package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.image.dto.ImageResponseDto;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.vote.dto.VoteResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public abstract class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long clickCount;
    private List<ImageResponseDto> imageResponseDtos;
    private VoteResponseDto voteResponseDto;

    protected PostResponseDto(Post post,List<ImageResponseDto> imageResponseDtos,VoteResponseDto voteResponseDto) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userId=post.getUser().getId();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.clickCount= post.getClickCount();
        this.imageResponseDtos=imageResponseDtos;
        this.voteResponseDto=voteResponseDto;
    }
}
