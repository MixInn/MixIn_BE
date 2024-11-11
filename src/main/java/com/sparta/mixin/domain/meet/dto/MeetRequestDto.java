package com.sparta.mixin.domain.meet.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
public class MeetRequestDto {
    private String type;
    private String category;
    @Setter
    private String image;
    private String name;
    private String info;
    private String rule;
    private String tag;

}
