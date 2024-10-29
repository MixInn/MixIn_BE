package com.sparta.mixin.domain.meetannouncement.dto;

import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import lombok.Getter;

@Getter
public class MeetAnnouncementResponseDto {
    private String recruitmentPeriod; // 모집기간
    private String gender; // 성별
    private Integer numberOfPeople; // 인원수
    private String preferences; // 우대사항
    private String meetingFrequency; // 모임주기
    private String approvalType; // 승인여부
    private String applicationForm;

    public MeetAnnouncementResponseDto(MeetAnnouncement meetAnnouncement) {
        this.recruitmentPeriod = meetAnnouncement.getRecruitmentPeriod();
        this.gender = meetAnnouncement.getGender();
        this.numberOfPeople = meetAnnouncement.getNumberOfPeople();
        this.preferences = meetAnnouncement.getPreferences();
        this.meetingFrequency = meetAnnouncement.getMeetingFrequency();
        this.approvalType = meetAnnouncement.getApprovalType();
        this.applicationForm = meetAnnouncement.getApplicationForm();
    }
}
