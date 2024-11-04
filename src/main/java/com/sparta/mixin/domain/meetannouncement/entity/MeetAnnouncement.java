package com.sparta.mixin.domain.meetannouncement.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "MeetAnnouncement")
@NoArgsConstructor
@Getter
public class MeetAnnouncement extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;


    private LocalDateTime recruitmentPeriod; // 모집기간
    // 성별 enum으로 바꿔서 관리
    private GenderRestriction gender; // 성별
    private int numberOfPeople; // 인원수
    private String preferences; // 우대사항
    private String meetingFrequency; // 모임주기
    private ApprovalType approvalType; // 승인여부
    private String applicationForm;

    @Builder
    public MeetAnnouncement(Meet meet, String recruitmentPeriod, String gender, int numberOfPeople, String preferences, String meetingFrequency,String approvalType, String applicationForm){
        this.meet = meet;
        this.recruitmentPeriod = LocalDateTime.parse(recruitmentPeriod);
        this.gender = GenderRestriction.fromString(gender);
        this.numberOfPeople = numberOfPeople;
        this.preferences = preferences;
        this.meetingFrequency = meetingFrequency;
        this.approvalType = ApprovalType.fromString(approvalType);
        this.applicationForm = applicationForm;
    }

    public void updateMeetAnnouncement(MeetAnnouncementRequestDto requestDto){
        if(requestDto.getRecruitmentPeriod() != null){
            this.recruitmentPeriod = LocalDateTime.parse(requestDto.getRecruitmentPeriod());
        }
        if(requestDto.getGender() != null){
            this.gender = GenderRestriction.fromString(requestDto.getGender());
        }
        if(requestDto.getNumberOfPeople() != null){
            this.numberOfPeople = requestDto.getNumberOfPeople();
        }
        if(requestDto.getPreferences() != null){
            this.preferences = requestDto.getPreferences();
        }
        if(requestDto.getMeetingFrequency() != null){
            this.meetingFrequency = requestDto.getMeetingFrequency();
        }
        if(requestDto.getApprovalType() != null){
            this.approvalType = ApprovalType.fromString(requestDto.getApprovalType());
        }
        if(requestDto.getApplicationForm() != null){
            this.applicationForm = requestDto.getApplicationForm();
        }


    }
}