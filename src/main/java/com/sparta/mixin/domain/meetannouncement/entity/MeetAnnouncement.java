package com.sparta.mixin.domain.meetannouncement.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    private String recruitmentPeriod; // 모집기간
    private String gender; // 성별
    private int numberOfPeople; // 인원수
    private String preferences; // 우대사항
    private String meetingFrequency; // 모임주기
    private String approvalType; // 승인여부

    private String applicationForm;


    // Getters and setters

    @Builder
    public MeetAnnouncement(Meet meet, String recruitmentPeriod, String gender, int numberOfPeople, String preferences, String meetingFrequency,String approvalType, String applicationForm){
        this.meet = meet;
        this.recruitmentPeriod = recruitmentPeriod;
        this.gender = gender;
        this.numberOfPeople = numberOfPeople;
        this.preferences = preferences;
        this.meetingFrequency = meetingFrequency;
        this.approvalType = approvalType;
        this.applicationForm = applicationForm;
    }

    public void updateMeetAnnouncement(MeetAnnouncementRequestDto requestDto){
        if(requestDto.getRecruitmentPeriod() != null){
            this.recruitmentPeriod = requestDto.getRecruitmentPeriod();
        }
        if(requestDto.getGender() != null){
            this.gender = requestDto.getGender();
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
            this.approvalType = requestDto.getApprovalType();
        }
        if(requestDto.getApplicationForm() != null){
            this.applicationForm = requestDto.getApplicationForm();
        }


    }
}