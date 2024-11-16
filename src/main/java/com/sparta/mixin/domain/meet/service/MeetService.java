package com.sparta.mixin.domain.meet.service;

import com.sparta.mixin.domain.meet.dto.MeetRequestDto;
import com.sparta.mixin.domain.meet.entity.*;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncementService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetService {
    private static final Logger log = LoggerFactory.getLogger(MeetService.class);
    private final MeetRepository meetRepository;
    private final MeetAuthorizationService meetAuthorizationService;
    private final MeetAnnouncementService meetAnnouncementService;


    /**
     * 새로운 모임을 생성하는 메서드
     * @param requestDto 모임 생성 요청 데이터
     * @param user 요청을 보낸 사용자 (생성자)
     * @return 생성된 모임
     */
    @Transactional
    public Meet createMeet(MeetRequestDto requestDto, User user) {
        log.info("모임 생성 시작 - 사용자 ID: {}, 요청 데이터: {}", user.getId(), requestDto);

        MeetType meetType = MeetType.fromString(requestDto.getType());
        MeetCategory meetCategory = MeetCategory.fromString(requestDto.getCategory());
        log.debug("모임 타입: {}, 카테고리: {}", meetType, meetCategory);

        Meet meet = Meet.builder()
                .type(meetType)
                .meetCategory(meetCategory)
                .image(requestDto.getImage())
                .name(requestDto.getName())
                .info(requestDto.getInfo())
                .rule(requestDto.getRule())
                .tag(requestDto.getTag())
                .build();

        Meet savedMeet = meetRepository.save(meet);
        log.info("모임 생성 완료 - 모임 ID: {}", savedMeet.getId());

        MeetAuthorization meetAuthorization = MeetAuthorization.builder()
                .meet(savedMeet)
                .user(user)
                .authorization(AuthorizationLevel.LEADER)
                .build();
        meetAuthorizationService.save(meetAuthorization);
        log.debug("리더 권한 부여 완료 - 사용자 ID: {}", user.getId());

        if (meetType == MeetType.LIGHTNING) {
            MeetAnnouncementRequestDto announcementRequestDto = MeetAnnouncementRequestDto.fromRequest(requestDto);
            MeetAnnouncement meetAnnouncement = meetAnnouncementService.createLightingAnnouncement(savedMeet, announcementRequestDto, user);
            log.info("번개 모임 공고 생성 완료 - 모임 공고 ID: {}", meetAnnouncement.getId());
        }

        log.info("모임 생성이 성공 - 모임 ID: {}", savedMeet.getId());
        return savedMeet;
    }

    /**
     * 모임 정보를 업데이트하는 메서드
     */
    @Transactional
    public void updateMeet(Long meetId, MeetRequestDto requestDto, User user) {
        log.info("모임 업데이트 시작 - 사용자 ID: {}, 모임 ID: {}, ", user.getId(), meetId);

        Meet meet = findById(meetId);
        log.debug("모임 조회 완료 - 모임 ID: {}", meet.getId());

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);
        log.debug("사용자 권한 확인 - 사용자 ID: {}, 권한: {}", user.getId(), userRole);

        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            log.warn("업데이트 권한 부족 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meetId);
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        meet.updateMeet(requestDto);
        log.info("모임 정보 업데이트 완료 - 모임 ID: {}", meetId);
    }

    /**
     * 모임 삭제 메서드
     */
    @Transactional
    public void deleteMeet(Long meetId, User user) {
        log.info("모임 삭제 시작 - 모임 ID: {}, 사용자 ID: {}", meetId, user.getId());

        Meet meet = meetRepository.findById(meetId)
                .orElseThrow(() -> {
                    log.error("삭제 실패: 존재하지 않는 모임. 모임 ID: {}", meetId);
                    return new CustomException(ErrorCode.MEET_NOT_FOUND);
                });

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);
        log.debug("사용자 권한 확인 - 사용자 ID: {}, 권한: {}", user.getId(), userRole);

        if (userRole != AuthorizationLevel.LEADER) {
            log.warn("삭제 권한 부족 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meetId);
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        meetRepository.deleteById(meetId);
        log.info("모임 삭제 완료 - 모임 ID: {}", meetId);
    }

    /**
     * ID로 모임을 찾는 유틸리티 메서드
     */
    public Meet findById(Long meetId) {
        log.debug("모임 조회 시도 - 모임 ID: {}", meetId);
        return meetRepository.findById(meetId)
                .orElseThrow(() -> {
                    log.error("모임 조회 실패 - 모임이 존재하지 않음 - 모임 ID: {}", meetId);
                    return new CustomException(ErrorCode.MEET_NOT_FOUND);
                });
    }
}
