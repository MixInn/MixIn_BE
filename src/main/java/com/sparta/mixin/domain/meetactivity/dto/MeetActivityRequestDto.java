package com.sparta.mixin.domain.meetactivity.dto;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MeetActivityRequestDto {
    private String activityPosition;
    private LocalDateTime date;
    private String content;
}
