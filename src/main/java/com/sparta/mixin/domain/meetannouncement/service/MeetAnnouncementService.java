package com.sparta.mixin.domain.meetannouncement.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetCategory;
import com.sparta.mixin.domain.meet.entity.MeetType;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementListRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementResponseDto;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncementRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetAnnouncementService {
    private final MeetAnnouncementRepository meetAnnouncementRepository;
    private final MeetAuthorizationService meetAuthorizationService;
    private final MeetService meetService;

    public Page<MeetAnnouncementResponseDto> getAnnouncementList(MeetAnnouncementListRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

        MeetType meetType = (requestDto.getMeetType() != null) ? MeetType.fromString(requestDto.getMeetType()) : null;
        MeetCategory category = (requestDto.getCategory() != null) ? MeetCategory.fromString(requestDto.getCategory()) : null;

        return meetAnnouncementRepository.findAnnouncementsWithFilters(
                meetType,
                category,
                requestDto.getTags(),
                requestDto.getMeetName(),
                pageable
        ).map(MeetAnnouncementResponseDto::new);
    }

    public void createMeetAnnouncement(Long meetId, MeetAnnouncementRequestDto requestDto, User currentUser) {
        Meet meet = meetService.findById(meetId);

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, currentUser);

        // 사용자 권한 확인 ( 리더인 경우 생성 가능 )
        if (userRole != AuthorizationLevel.LEADER ) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        MeetAnnouncement meetAnnouncement = MeetAnnouncement.builder()
                .meet(meet)
                .recruitmentPeriod(requestDto.getRecruitmentPeriod())
                .gender(requestDto.getGender())
                .numberOfPeople(requestDto.getNumberOfPeople())
                .preferences(requestDto.getPreferences())
                .meetingFrequency(requestDto.getMeetingFrequency())
                .approvalType(requestDto.getApprovalType())
                .applicationForm(requestDto.getApplicationForm())
                .build();

        // 모임 공고 생성
        meetAnnouncementRepository.save(meetAnnouncement);
    }

    public void updateMeetAnnouncement(Long meetId, MeetAnnouncementRequestDto requestDto, User currentUser) {
        MeetAnnouncement meetAnnouncement = meetAnnouncementRepository.findByMeetId(meetId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meetAnnouncement.getMeet(), currentUser);

        // 사용자 권한 확인 ( 리더 or 부리더인 경우 수정 가능 )
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 모임 공고 수정
        meetAnnouncement.updateMeetAnnouncement(requestDto);
    }

    public MeetAnnouncementResponseDto readMeetAnnouncement(Long meetId) {
        MeetAnnouncement meetAnnouncement = findByMeetId(meetId);
        MeetAnnouncementResponseDto responseDto = new MeetAnnouncementResponseDto(meetAnnouncement);
        return responseDto;
    }

    public MeetAnnouncement findByMeetId(Long meetId) {
        return meetAnnouncementRepository.findByMeetId(meetId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
