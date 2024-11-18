package com.sparta.mixin.domain.meetactivity.dto;

import com.sparta.mixin.domain.meetactivity.entity.MeetActivity;
import com.sparta.mixin.domain.user.dto.UserResponseDto;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MeetActivityResponseDto {
    private Long activityId;
    private String position;
    private LocalDate date;
    private String content;
    private List<UserResponseDto> participants;

    public MeetActivityResponseDto(MeetActivity meetActivity) {
        this.activityId = meetActivity.getActivityId();
        this.position = meetActivity.getPosition();
        this.date = meetActivity.getDate();
        this.content = meetActivity.getContent();
        this.participants = meetActivity.getParticipants().stream()
                .map(participation -> new UserResponseDto(participation.getUser()))
                .collect(Collectors.toList());
    }
}
