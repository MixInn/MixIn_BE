package com.sparta.mixin.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UniversitySearchRequestDto {
    @NotNull(message = "학교 이름을 입력해주세요.")
    private String universityName;
}
