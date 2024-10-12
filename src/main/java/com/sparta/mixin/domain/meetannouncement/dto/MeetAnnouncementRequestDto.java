package com.sparta.mixin.domain.meetannouncement.dto;

import lombok.Getter;

@Getter
public class MeetAnnouncementRequestDto {
    private String recruitmentPeriod; // 모집기간
    private String gender; // 성별
    private Integer numberOfPeople; // 인원수
    private String preferences; // 우대사항
    private String meetingFrequency; // 모임주기
    private String approvalType; // 승인여부
    private String applicationForm;
}
