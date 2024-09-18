package com.sparta.mixin.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MajorSearchRequestDto {
    @NotNull(message = "학과 이름을 입력해주세요.")
    private String major;
}
