package com.sparta.mixin.domain.image.dto;

import com.sparta.mixin.domain.image.entity.EntityType;
import com.sparta.mixin.domain.image.entity.Image;

public class ImageResponseDto {
    private Long id;
    private String imageUrl;
    private EntityType entityType;
    private Long entityId;

    public ImageResponseDto(Image image) {
        this.id= image.getId();
        this.imageUrl= image.getImageUrl();
        this.entityType=image.getEntityType();
        this.entityId=image.getMeetPost().getId(); // 밋 커뮤니티의 경우
    }
}
