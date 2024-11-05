package com.sparta.mixin.domain.meetapplication.dto;

import com.sparta.mixin.domain.meetapplication.entity.MeetApplication;
import lombok.Getter;

@Getter
public class MeetApplicationResponseDto {
    private Long meetApplicationId;
    private Long userId;
    private String status;
    private String reason;

    public MeetApplicationResponseDto(MeetApplication application) {
        this.meetApplicationId = application.getId();
        this.userId = application.getUser().getId();
        this.status = application.getResultEnum().getStatus();
        this.reason = application.getReason();
    }


}
