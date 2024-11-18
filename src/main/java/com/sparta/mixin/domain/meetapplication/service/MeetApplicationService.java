package com.sparta.mixin.domain.meetapplication.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetAuthorization;
import com.sparta.mixin.domain.meet.entity.MeetRepository;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meetannouncement.entity.ApprovalType;
import com.sparta.mixin.domain.meetannouncement.entity.GenderRestriction;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncementService;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationRequestDto;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationResponseDto;
import com.sparta.mixin.domain.meetapplication.entity.MeetApplication;
import com.sparta.mixin.domain.meetapplication.entity.MeetApplicationRepository;
import com.sparta.mixin.domain.user.entity.Gender;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetApplicationService {
    private final MeetApplicationRepository meetApplicationRepository;
    private final MeetRepository meetRepository;
    private final MeetAuthorizationService meetAuthorizationService;
    private final MeetAnnouncementService meetAnnouncementService;
    private static final Logger log = LoggerFactory.getLogger(MeetApplicationService.class);

    /**
     * 사용자가 모임 공고에 지원서를 생성하는 메서드.
     *
     * @param meetAnnouncementId 모임 공고 ID
     * @param requestDto 사용자의 모임 지원서 정보
     * @param user 모임에 지원하는 사용자
     */
    @Transactional
    public void createMeetApplication(Long meetAnnouncementId, MeetApplicationRequestDto requestDto, User user) {
        log.info("모임 지원서 생성 시작 - 사용자 ID: {}, 모임 공고 ID: {}", user.getId(), meetAnnouncementId);

        // 모임 공고 확인
        MeetAnnouncement meetAnnouncement = meetAnnouncementService.findById(meetAnnouncementId);
        Meet meet = meetAnnouncement.getMeet();

        // 이미 모임의 회원인지 확인
        boolean isMember = meetAuthorizationService.isUserMemberOfMeet(meet, user);
        if (isMember) {
            log.warn("이미 회원인 사용자 - 사용자 ID: {}", user.getId());
            throw new CustomException(ErrorCode.USER_ALREADY_JOINED);
        }

        // 이미 신청한 사용자 확인
        if (meetApplicationRepository.existsByMeetAnnouncementAndUser(meetAnnouncement, user)) {
            log.warn("이미 신청한 사용자 - 사용자 ID: {}, 모임 공고 ID: {}", user.getId(), meetAnnouncementId);
            throw new CustomException(ErrorCode.ALREADY_APPLIED);
        }

        // 성별 제한 확인
        if (meetAnnouncement.getGender() != GenderRestriction.NO_RESTRICTION) {
            if (!isGenderMatch(meetAnnouncement.getGender(), user.getGender())) {
                log.warn("성별 제한 불일치 - 사용자 성별: {}, 제한 성별: {}", user.getGender(), meetAnnouncement.getGender());
                throw new CustomException(ErrorCode.GENDER_RESTRICTION_NOT_ALLOWED);
            }
        }

        MeetApplication meetApplication = MeetApplication.builder()
                .meetAnnouncement(meetAnnouncement)
                .user(user)
                .content(requestDto.getContent())
                .build();

        // 저장
        meetApplicationRepository.save(meetApplication);
        log.info("모임 지원서 저장 완료 - 사용자 ID: {}, 모임 공고 ID: {}", user.getId(), meetAnnouncementId);

        // approvalType이 OPEN일 경우 바로 가입 처리
        if (meetAnnouncement.getApprovalType() == ApprovalType.OPEN) {
            log.info("승인 타입이 OPEN - 자동으로 지원서 수락 처리 시작");
            acceptMeetApplication(meetApplication.getId());
        }
    }

    /**
     * 사용자가 제출한 모임 지원서를 수정하는 메서드.
     *
     * @param meetApplicationId 수정할 모임 지원서 ID
     * @param requestDto 수정할 내용이 담긴 DTO
     * @param user 지원서를 수정하는 사용자
     */
    @Transactional
    public void updateMeetApplication(Long meetApplicationId, MeetApplicationRequestDto requestDto, User user) {
        log.info("모임 지원서 수정 시작 - 지원서 ID: {}, 사용자 ID: {}", meetApplicationId, user.getId());

        MeetApplication meetApplication = findById(meetApplicationId);

        if (!meetApplication.getUser().getId().equals(user.getId())) {
            log.error("지원서 소유자 불일치 - 사용자 ID: {}, 지원서 사용자 ID: {}", user.getId(), meetApplication.getUser().getId());
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        meetApplication.updateContent(requestDto.getContent());
        meetApplicationRepository.save(meetApplication);
        log.info("모임 지원서 수정 완료 - 지원서 ID: {}", meetApplicationId);
    }

    /**
     * 사용자가 제출한 모임 지원서를 삭제하는 메서드.
     *
     * @param meetApplicationId 삭제할 모임 지원서 ID
     * @param user 지원서를 삭제하는 사용자
     */
    @Transactional
    public void deleteMeetApplication(Long meetApplicationId, User user) {
        log.info("모임 지원서 삭제 시작 - 지원서 ID: {}, 사용자 ID: {}", meetApplicationId, user.getId());

        MeetApplication meetApplication = findById(meetApplicationId);

        if (!meetApplication.getUser().getId().equals(user.getId())) {
            log.error("지원서 소유자 불일치 - 사용자 ID: {}, 지원서 사용자 ID: {}", user.getId(), meetApplication.getUser().getId());
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 해당 지원서로 생성된 MeetAuthorization 삭제
        Meet meet = meetApplication.getMeetAnnouncement().getMeet();
        Optional<MeetAuthorization> meetAuthorization = meetAuthorizationService.findByMeetAndUser(meet, user);

        meetAuthorization.ifPresent(auth -> {
            meetAuthorizationService.deleteMeetAuthorization(auth);
            log.info("모임 권한 삭제 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meet.getId());
        });

        meetApplicationRepository.delete(meetApplication);
        log.info("모임 지원서 삭제 완료 - 지원서 ID: {}", meetApplicationId);
    }

    /**
     * 모임 지원서를 수락하고 사용자를 모임에 추가하는 메서드.
     *
     * @param meetApplicationId 수락할 모임 지원서 ID
     */
    @Transactional
    public void acceptMeetApplication(Long meetApplicationId) {
        log.info("모임 지원서 수락 시작 - 지원서 ID: {}", meetApplicationId);

        MeetApplication meetApplication = findById(meetApplicationId);

        // 수락 상태로 변경
        meetApplication.acceptApplication();

        // 신청이 수락되면 MeetAuthorization에 추가
        Meet meet = meetApplication.getMeetAnnouncement().getMeet();
        User user = meetApplication.getUser();

        // 기본 권한을 MEMBER로 설정
        MeetAuthorization meetAuthorization = MeetAuthorization.builder()
                .meet(meet)
                .user(user)
                .authorization(AuthorizationLevel.MEMBER)  // 기본적으로 MEMBER 권한 부여
                .build();

        // MeetAuthorization 저장
        meetAuthorizationService.save(meetAuthorization);
        log.info("모임 권한 부여 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meet.getId());

        // 업데이트된 상태 저장
        meetApplicationRepository.save(meetApplication);
        log.info("모임 지원서 수락 완료 - 지원서 ID: {}", meetApplicationId);
    }

    /**
     * 모임 지원서를 거절하고 거절 사유를 저장하는 메서드.
     *
     * @param meetApplicationId 거절할 모임 지원서 ID
     * @param reason 거절 사유
     */
    @Transactional
    public void rejectMeetApplication(Long meetApplicationId, String reason) {
        log.info("모임 지원서 거절 시작 - 지원서 ID: {}, 사유: {}", meetApplicationId, reason);

        MeetApplication meetApplication = findById(meetApplicationId);

        // 거절 상태로 변경
        meetApplication.rejectApplication(reason);

        // 업데이트된 상태 저장
        meetApplicationRepository.save(meetApplication);
        log.info("모임 지원서 거절 완료 - 지원서 ID: {}", meetApplicationId);
    }

    /**
     * 사용자가 제출한 모든 모임 지원서를 조회하는 메서드.
     *
     * @param user 지원서를 조회할 사용자
     * @return 사용자의 모든 모임 지원서 리스트
     */
    public List<MeetApplicationResponseDto> getMyApplications(User user) {
        log.info("사용자 ID: {} 의 모든 모임 지원서 조회", user.getId());

        List<MeetApplication> applications = meetApplicationRepository.findByUser(user);
        return applications.stream()
                .map(MeetApplicationResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 모임에 대한 모든 지원서를 조회하는 메서드.
     *
     * @param meetId 모임 ID
     * @param user 지원서를 조회하는 사용자
     * @return 모임에 대한 모든 지원서 리스트
     */
    public List<MeetApplicationResponseDto> getApplicationsForMeet(Long meetId, User user) {
        log.info("모임 ID: {} 의 모든 지원서 조회 - 사용자 ID: {}", meetId, user.getId());

        Meet meet = meetRepository.findById(meetId).orElseThrow(() -> new CustomException(ErrorCode.MEET_NOT_FOUND));
        MeetAnnouncement meetAnnouncement = meet.getMeetAnnouncement();

        AuthorizationLevel role = meetAuthorizationService.getUserRole(meet, user);

        if (role != AuthorizationLevel.LEADER) {
            log.error("모임 리더가 아닌 사용자 접근 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meetId);
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<MeetApplication> applications = meetApplicationRepository.findByMeetAnnouncement(meetAnnouncement);
        log.info("모임 ID: {} 의 지원서 조회 완료 - 조회된 지원서 수: {}", meetId, applications.size());

        return applications.stream()
                .map(MeetApplicationResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 지원서 ID로 모임 지원서를 조회하는 메서드.
     *
     * @param meetApplicationId 조회할 모임 지원서 ID
     * @return 모임 지원서 객체
     */
    public MeetApplication findById(Long meetApplicationId) {
        return meetApplicationRepository.findById(meetApplicationId)                .orElseThrow(() -> {
            log.error("지원서 조회 실패 - 지원서 ID: {}", meetApplicationId);
            return new CustomException(ErrorCode.APPLICATION_NOT_FOUND);
        });
    }

    /**
     * 성별 제한을 확인하는 메서드
     *
     * @param genderRestriction 모임의 성별 제한
     * @param userGender 사용자의 성별
     * @return 성별 제한을 통과하면 true, 아니면 false
     */
    private boolean isGenderMatch(GenderRestriction genderRestriction, Gender userGender) {
        if (genderRestriction == GenderRestriction.NO_RESTRICTION) {
            return true;
        }
        return userGender == (genderRestriction == GenderRestriction.MALE_ONLY ? Gender.MALE : Gender.FEMALE);
    }
}
