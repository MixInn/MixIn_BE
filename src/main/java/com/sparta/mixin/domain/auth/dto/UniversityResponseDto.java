package com.sparta.mixin.domain.auth.dto;

import lombok.Getter;

@Getter
public class UniversityResponseDto {
    private String universityName;
    private String universityAddress;

    public UniversityResponseDto(String universityName, String universityAddress) {
        this.universityName = universityName;
        this.universityAddress = universityAddress;
    }
}
