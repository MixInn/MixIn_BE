package com.sparta.mixin.domain.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SmsRequestDto {

    @NotEmpty(message = "휴대폰 번호를 입력해주세요.")
    private String PhoneNumber;
}
