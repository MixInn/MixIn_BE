package com.sparta.mixin.domain.auth.dto;

import lombok.Getter;

@Getter
public class MajorResponseDto {
    private String category;
    private String majorName;

    public MajorResponseDto(String category, String majorName) {
        this.category = category;
        this.majorName = majorName;
    }
}
