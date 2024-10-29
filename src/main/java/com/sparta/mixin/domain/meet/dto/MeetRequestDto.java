package com.sparta.mixin.domain.meet.dto;

import com.sparta.mixin.domain.meet.entity.MeetCategory;
import com.sparta.mixin.domain.meet.entity.MeetType;
import lombok.Getter;

@Getter
public class MeetRequestDto {
    private String type;
    private String category;
    private String image;
    private String name;
    private String info;
    private String rule;
    private String tag;

}
