package com.sparta.mixin.domain.meetannouncement.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementResponseDto;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncmentRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetAnnouncmentService {
    private final MeetAnnouncmentRepository meetAnnouncmentRepository;
    private final MeetAuthorizationService meetAuthorizationService;
    private final MeetService meetService;

    public void createMeetAnnouncment(Long meetId, MeetAnnouncementRequestDto requestDto) {
        Meet meet = meetService.findMeetById(meetId);

        // 임시 유저
        User currentUser = new User();

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, currentUser);

        // 사용자 권한 확인 ( 리더 or 부리더인 경우 수정 가능 )
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        MeetAnnouncement meetAnnouncement = MeetAnnouncement.builder()
                .meet(meet)
                .recruitmentPeriod(requestDto.getRecruitmentPeriod())
                .gender(requestDto.getGender())
                .numberOfPeople(requestDto.getNumberOfPeople())
                .meetingFrequency(requestDto.getMeetingFrequency())
                .approvalType(requestDto.getApprovalType())
                .applicationForm(requestDto.getApplicationForm())
                .build();

        // 모임 공고 생성
        meetAnnouncmentRepository.save(meetAnnouncement);


    }

    public void updateMeetAnnouncment(Long meetId, MeetAnnouncementRequestDto requestDto) {
        MeetAnnouncement meetAnnouncement = meetAnnouncmentRepository.findByMeetId(meetId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 임시 유저
        User currentUser = new User();

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meetAnnouncement.getMeet(), currentUser);

        // 사용자 권한 확인 ( 리더 or 부리더인 경우 수정 가능 )
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 모임 공고 수정
        meetAnnouncement.updateMeetAnnouncement(requestDto);
    }

    public MeetAnnouncementResponseDto readMeetAnnouncment(Long meetId) {
        MeetAnnouncement meetAnnouncement = findByMeetId(meetId);
        MeetAnnouncementResponseDto responseDto = new MeetAnnouncementResponseDto(meetAnnouncement);
        return responseDto;
    }

    public MeetAnnouncement findByMeetId(Long meetId) {
        return meetAnnouncmentRepository.findByMeetId(meetId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
