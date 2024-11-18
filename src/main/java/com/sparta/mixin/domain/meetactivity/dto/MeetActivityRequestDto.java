package com.sparta.mixin.domain.meetactivity.dto;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MeetActivityRequestDto {
    private String title;
    private String position;
    private LocalDate date;
    private String content;
}
