package com.sparta.mixin.domain.image.dto;

import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.post.PostType;
import lombok.Getter;

@Getter
public class ImageResponseDto {
    private Long id;
    private String imageUrl;
    private PostType postType;
    private Long entityId;

    public ImageResponseDto(Image image) {
        this.id= image.getId();
        this.imageUrl= image.getImageUrl();
        this.postType=image.getPostType();
        this.entityId=image.getPost().getId();
    }
}
