package com.sparta.mixin.domain.meetannouncement.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MeetAnnouncementRequestDto {
    // 공통
    private String recruitmentPeriod; // 모집기간
    private String gender; // 성별
    private String tag;
    private Integer numberOfPeople; // 인원수

    // 번개만
    private String meetTime;

    // 일반 공고
    private String preferences; // 우대사항
    private String meetingFrequency; // 모임주기
    private String approvalType; // 승인여부
    private String applicationForm;
}
