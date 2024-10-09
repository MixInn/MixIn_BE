package com.sparta.mixin.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SmsCertificationRequestDto {
    private String phoneNumber;
    @NotNull(message = "인증번호를 입력해주세요.")
    private String code;

}
