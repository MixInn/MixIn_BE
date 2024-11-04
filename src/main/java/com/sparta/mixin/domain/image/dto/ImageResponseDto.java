package com.sparta.mixin.domain.image.dto;

import com.sparta.mixin.domain.image.entity.EntityType;
import com.sparta.mixin.domain.image.entity.Image;
import lombok.Getter;

@Getter
public class ImageResponseDto {
    private Long id;
    private String imageUrl;
    private EntityType entityType;
    private Long entityId;

    public ImageResponseDto(Image image) {
        this.id= image.getId();
        this.imageUrl= image.getImageUrl();
        this.entityType=image.getEntityType();
        if(image.getMeetPost()!=null){
            this.entityId=image.getMeetPost().getId();
        }if(image.getPublicPost()!=null){
            this.entityId=image.getPublicPost().getId();
        }
    }
}
