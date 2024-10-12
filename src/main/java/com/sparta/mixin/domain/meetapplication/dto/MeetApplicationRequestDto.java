package com.sparta.mixin.domain.meetapplication.dto;

import lombok.Getter;

@Getter
public class MeetApplicationRequestDto {
    private Long userId;
    private Long meetId;
    private String content;
}
