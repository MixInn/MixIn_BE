package com.sparta.mixin.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.mixin.domain.image.dto.ImageResponseDto;
import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.vote.dto.VoteResponseDto;
import com.sparta.mixin.domain.user.dto.UserResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String userNickname;
    private UserResponseDto userResponseDto;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long clickCount;
    private List<ImageResponseDto> imageResponseDtos;
    private VoteResponseDto voteResponseDto;
    private String firstImageUrl;

    protected PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userNickname=post.getUser().getName();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.clickCount= post.getClickCount();
        this.firstImageUrl=extractFirstImageUrl(post);
    }

    protected PostResponseDto(Post post, List<ImageResponseDto> imageResponseDtos,VoteResponseDto voteResponseDto) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userResponseDto=new UserResponseDto(post.getUser());
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.clickCount= post.getClickCount();
        this.imageResponseDtos=imageResponseDtos;
        this.voteResponseDto=voteResponseDto;
    }

    // 이미지 리스트에서 첫 번째 URL 추출
    private String extractFirstImageUrl(Post post) {
        List<Image> images = post.getPostImages();
        if (images != null && !images.isEmpty()) {
            return images.get(0).getImageUrl();
        }
        return null;
    }
}
