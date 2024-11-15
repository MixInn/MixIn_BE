package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.post.entity.Post;
import java.time.LocalDateTime;
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

    protected PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userId=post.getUser().getId();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.clickCount= post.getClickCount();
    }
}
