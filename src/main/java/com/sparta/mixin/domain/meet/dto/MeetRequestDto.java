package com.sparta.mixin.domain.meet.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
public class MeetRequestDto {
    // 공통
    private String type;
    private String category;
    private String name;
    private String info;
    private String tag;

    // 일반 모임
    @Setter
    private String image;
    private String rule;

    // 번개모임 생성
    private String recruitmentPeriod; // 모집기간
    private String meetTime;
    private String gender; // 성별
    private Integer numberOfPeople; // 인원수
}
