package com.sparta.mixin.domain.meetannouncement.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetType;
import com.sparta.mixin.domain.meetannouncement.bookmark.entity.AnnouncementBookmark;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetapplication.entity.MeetApplication;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MeetAnnouncement")
@NoArgsConstructor
@Getter
public class MeetAnnouncement extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    @Enumerated(EnumType.STRING)
    private MeetType meetType;  // 모임 타입 필드 추가

    private LocalDate recruitmentPeriod; // 모집기간
    private LocalTime meetTime;
    private GenderRestriction gender; // 성별
    private int numberOfPeople; // 인원수
    private String preferences; // 우대사항
    private String meetingFrequency; // 모임주기
    private String location;
    private ApprovalType approvalType; // 승인여부
    private String applicationForm;
    private String tag;
    private String university;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int bookmarkCount = 0;

    @OneToMany(mappedBy = "meetAnnouncement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetApplication> meetApplications = new ArrayList<>();

    @OneToMany(mappedBy = "meetAnnouncement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnnouncementBookmark> announcementBookmarks = new ArrayList<>();



    @Builder
    public MeetAnnouncement(Meet meet, String location, String recruitmentPeriod,String meetTime,String gender, int numberOfPeople,String tag, String preferences, String meetingFrequency,ApprovalType approvalType, String applicationForm,String university){
        this.meet = meet;
        this.meetType = meet.getType(); // Meet의 타입을 공고에 저장
        this.recruitmentPeriod = LocalDate.parse(recruitmentPeriod);
        if (meetTime != null) {
            this.meetTime = LocalTime.parse(meetTime);
        }
        this.location = location;
        this.gender = GenderRestriction.fromString(gender);
        this.tag = tag;
        this.numberOfPeople = numberOfPeople;
        this.preferences = preferences;
        this.meetingFrequency = meetingFrequency;
        this.approvalType = approvalType;
        this.applicationForm = applicationForm;
        this.university = university;
    }

    public void updateMeetAnnouncement(MeetAnnouncementRequestDto requestDto){
        if(requestDto.getRecruitmentPeriod() != null){
            this.recruitmentPeriod = LocalDate.parse(requestDto.getRecruitmentPeriod());
        }
        if(requestDto.getMeetTime() != null){
            this.meetTime = LocalTime.parse(requestDto.getMeetTime());
        }
        if (requestDto.getLocation() != null){
            this.location = requestDto.getLocation();
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

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementBookmarkCount() {
        this.bookmarkCount++;
    }

    public void decrementBookmarkCount() {
        if (this.bookmarkCount > 0) {
            this.bookmarkCount--;
        }
    }
}