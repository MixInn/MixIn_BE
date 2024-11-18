package com.sparta.mixin.domain.image.dto;

import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.post.PostType;
import lombok.Getter;

@Getter
public class ImageResponseDto {
    private Long id;
    private String imageUrl;

    public ImageResponseDto(Image image) {
        this.id= image.getId();
        this.imageUrl= image.getImageUrl();
    }
}
