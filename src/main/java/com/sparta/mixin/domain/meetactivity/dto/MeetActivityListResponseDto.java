package com.sparta.mixin.domain.meetactivity.dto;

import com.sparta.mixin.domain.meetactivity.entity.MeetActivity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
public class MeetActivityListResponseDto {
    private String title;             // 활동 제목
    private LocalDate date;           // 활동 날짜
    private int dday;                 // 디데이
    private String position;          // 활동 장소
    private boolean isParticipating;  // 로그인한 사용자의 참석 여부
    private int currentParticipants;  // 현재 참석 인원
    private int totalMembers;         // 모임의 총 멤버 수

    public MeetActivityListResponseDto(MeetActivity activity, int totalMembers, boolean isParticipating) {
        this.title = activity.getTitle();
        this.date = activity.getDate();
        this.dday = calculateDday(activity.getDate());
        this.position = activity.getPosition();
        this.isParticipating = isParticipating;
        this.currentParticipants = activity.getParticipants().size();
        this.totalMembers = totalMembers;
    }

    // D-day 계산
    private int calculateDday(LocalDate date) {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), date);
    }
}