package com.sparta.mixin.domain.meetannouncement.service;

import com.sparta.mixin.domain.meet.entity.*;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementListRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementResponseDto;
import com.sparta.mixin.domain.meetannouncement.entity.ApprovalType;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncementRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetAnnouncementService {
    private final MeetAnnouncementRepository meetAnnouncementRepository;
    private final MeetRepository meetRepository;
    private final MeetAuthorizationService meetAuthorizationService;
    private static final Logger log = LoggerFactory.getLogger(MeetAnnouncementService.class);

    /**
     * 모임 공고 목록을 조회하는 메서드
     * @param requestDto 요청된 페이지와 필터 정보를 담은 DTO
     * @param currentUser 현재 로그인한 사용자
     * @return 필터링된 모임 공고 목록
     */
    public Page<MeetAnnouncementResponseDto> getAnnouncementList(MeetAnnouncementListRequestDto requestDto, User currentUser) {
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

        // 필터링된 모임 공고 조회
        MeetType meetType = (requestDto.getMeetType() != null) ? MeetType.fromString(requestDto.getMeetType()) : null;
        MeetCategory category = (requestDto.getCategory() != null) ? MeetCategory.fromString(requestDto.getCategory()) : null;

        log.debug("모임 타입: {}, 카테고리: {}", meetType, category);

        // 페이징 및 필터링된 결과를 조회
        Page<MeetAnnouncement> announcementsPage = meetAnnouncementRepository.findAnnouncementsWithFiltersAndSort(
                meetType,
                category,
                requestDto.getTags(),
                requestDto.getMeetName(),
                currentUser.getUniversity(),
                requestDto.getSortType(),
                pageable
        );

        // 각 공고마다 리더 정보를 조회하여 DTO로 변환
        Page<MeetAnnouncementResponseDto> responsePage = announcementsPage.map(meetAnnouncement -> {
            User leader = meetAuthorizationService.getMeetLeader(meetAnnouncement.getMeet().getId()); // 리더 조회
            return new MeetAnnouncementResponseDto(meetAnnouncement, leader); // DTO 생성
        });

        log.info("모임 공고 목록 조회 완료 - 총 공고 수: {}, 사용자 ID: {}", responsePage.getTotalElements(), currentUser.getId());

        return responsePage;
    }

    /**
     * 모임 공고를 생성하는 메서드
     * @param meetId 생성할 모임의 ID
     * @param requestDto 공고에 필요한 데이터
     * @param currentUser 공고를 생성하는 사용자
     */
    public void createMeetAnnouncement(Long meetId, MeetAnnouncementRequestDto requestDto, User currentUser) {
        log.info("모임 공고 생성 시작 - 사용자 ID: {}, 요청 데이터: {}", currentUser.getId(), requestDto);

        Meet meet = meetRepository.findById(meetId).orElseThrow(() -> {
            log.error("공고 생성할 모임 조회 실패 - 모임 ID {}", meetId);
            return new CustomException(ErrorCode.MEET_NOT_FOUND);
        });
        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, currentUser);

        // 사용자 권한 확인: 리더만 공고를 생성할 수 있음
        if (userRole != AuthorizationLevel.LEADER) {
            log.error("공고 생성 권한 없음 - 사용자 ID: {}, 모임 ID: {}", currentUser.getId(), meetId);
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 이미 공고가 존재하는 경우 예외 처리
        meetAnnouncementRepository.findByMeetId(meetId).ifPresent(meetAnnouncement -> {
            log.error("공고 중복 생성 - 모임 ID {}", meetId);
            throw new CustomException(ErrorCode.MEET_ANNOUNCEMENT_ALREADY_EXISTS);
        });

        // 모임 공고 생성
        MeetAnnouncement meetAnnouncement = MeetAnnouncement.builder()
                .meet(meet)
                .recruitmentPeriod(requestDto.getRecruitmentPeriod())
                .gender(requestDto.getGender())
                .numberOfPeople(requestDto.getNumberOfPeople())
                .preferences(requestDto.getPreferences())
                .meetingFrequency(requestDto.getMeetingFrequency())
                .approvalType(ApprovalType.fromString(requestDto.getApprovalType()))
                .applicationForm(requestDto.getApplicationForm())
                .tag(requestDto.getTag())
                .university(currentUser.getUniversity())
                .build();

        // 공고를 DB에 저장
        meetAnnouncementRepository.save(meetAnnouncement);

        log.info("모임 공고 생성 완료 - 모임 ID: {}", meetId);
    }

    /**
     * 번개 모임 공고를 생성하는 내부 메서드
     * @param meet 모임 엔티티
     * @param requestDto 공고에 필요한 데이터
     * @param user 공고를 생성하는 사용자
     * @return 생성된 번개 모임 공고
     */
    public MeetAnnouncement createLightingAnnouncement(Meet meet, MeetAnnouncementRequestDto requestDto, User user) {
        log.info("번개 모임 공고 생성 시작 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meet.getId());

        MeetAnnouncement meetAnnouncement = MeetAnnouncement.builder()
                .meet(meet)
                .recruitmentPeriod(requestDto.getRecruitmentPeriod())
                .meetTime(requestDto.getMeetTime())
                .location(requestDto.getLocation())
                .gender(requestDto.getGender())
                .numberOfPeople(requestDto.getNumberOfPeople())
                .approvalType(ApprovalType.OPEN)
                .university(user.getUniversity())
                .tag(requestDto.getTag())
                .build();

        // 모임 공고 생성
        MeetAnnouncement createdAnnouncement = meetAnnouncementRepository.save(meetAnnouncement);

        log.info("번개 모임 공고 생성 완료 - 모임 ID: {}", meet.getId());

        return createdAnnouncement;
    }


    /**
     * 모임 공고를 업데이트하는 메서드
     * @param meetAnnouncementId 수정할 모임 공고의 ID
     * @param requestDto 업데이트할 공고 데이터
     * @param currentUser 공고를 수정하는 사용자
     */
    @Transactional
    public void updateMeetAnnouncement(Long meetAnnouncementId, MeetAnnouncementRequestDto requestDto, User currentUser) {
        log.info("모임 공고 수정 시작 - 사용자 ID: {}, 모임 공고 ID: {}", currentUser.getId(), meetAnnouncementId);

        MeetAnnouncement meetAnnouncement = findById(meetAnnouncementId);

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meetAnnouncement.getMeet(), currentUser);

        // 사용자 권한 확인 ( 리더 or 부리더인 경우 수정 가능 )
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            log.error("공고 수정 권한 없음 - 사용자 ID: {}, 모임 ID: {}", currentUser.getId(), meetAnnouncement.getMeet().getId());
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 모임 공고 수정
        meetAnnouncement.updateMeetAnnouncement(requestDto);

        log.info("모임 공고 수정 완료 - 모임 ID: {} 공고 ID: {}", meetAnnouncement.getMeet().getId(), meetAnnouncement.getId());
    }

    /**
     * 모임 공고를 조회하는 메서드
     * @param AnnouncementId 조회할 모임 공고의 ID
     * @return 모임 공고 DTO
     */
    @Transactional
    public MeetAnnouncementResponseDto readMeetAnnouncement(Long AnnouncementId) {
        log.info("모임 공고 조회 시작 - 모임 공고 ID: {}", AnnouncementId);

        MeetAnnouncement meetAnnouncement = findById(AnnouncementId);

        User leader = meetAuthorizationService.getMeetLeader(meetAnnouncement.getMeet().getId());

        MeetAnnouncementResponseDto responseDto = new MeetAnnouncementResponseDto(meetAnnouncement,leader);

        log.info("모임 공고 조회 완료 - 공고 ID: {}", meetAnnouncement.getId());

        // 조회수 증가
        meetAnnouncement.incrementViewCount();
        return responseDto;
    }


    /**
     * 모임 공고 ID로 공고를 조회하는 메서드
     * @param meetAnnouncementId 공고의 ID
     * @return 조회된 모임 공고
     */
    public MeetAnnouncement findById(Long meetAnnouncementId) {
        log.debug("공고 ID로 공고 조회 시작 - 공고 ID: {}", meetAnnouncementId);

        return meetAnnouncementRepository.findById(meetAnnouncementId).orElseThrow(() -> {
            log.error("공고 조회 실패 - 공고 ID: {}", meetAnnouncementId);
            return new CustomException(ErrorCode.MEET_ANNOUNCEMENT_NOT_FOUND);
        });
    }

}
