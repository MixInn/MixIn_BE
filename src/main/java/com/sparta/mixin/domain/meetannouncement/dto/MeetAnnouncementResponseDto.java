package com.sparta.mixin.domain.meetannouncement.dto;

import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.user.dto.UserResponseDto;
import com.sparta.mixin.domain.user.entity.User;
import lombok.Getter;

@Getter
public class MeetAnnouncementResponseDto {
    private Long meetId;
    private Long meetAnnouncementId;
    private String recruitmentPeriod; // 모집기간
    private String gender; // 성별
    private Integer numberOfPeople; // 인원수
    private String preferences; // 우대사항
    private String meetingFrequency; // 모임주기
    private String approvalType; // 승인여부
    private String applicationForm;
    private UserResponseDto leader;

    public MeetAnnouncementResponseDto(MeetAnnouncement meetAnnouncement, User user) {
        this.meetId = meetAnnouncement.getMeet().getId();
        this.meetAnnouncementId = meetAnnouncement.getId();
        this.recruitmentPeriod = meetAnnouncement.getRecruitmentPeriod().toString();
        this.gender = meetAnnouncement.getGender().getDescription();
        this.numberOfPeople = meetAnnouncement.getNumberOfPeople();
        this.preferences = meetAnnouncement.getPreferences();
        this.meetingFrequency = meetAnnouncement.getMeetingFrequency();
        this.approvalType = meetAnnouncement.getApprovalType().getDescription();
        this.applicationForm = meetAnnouncement.getApplicationForm();
        this.leader = new UserResponseDto(user);
    }
}
