package com.sparta.mixin.domain.meetactivity.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetRepository;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityListResponseDto;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityRequestDto;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityResponseDto;
import com.sparta.mixin.domain.meetactivity.entity.ActivityParticipation;
import com.sparta.mixin.domain.meetactivity.entity.ActivityParticipationRepository;
import com.sparta.mixin.domain.meetactivity.entity.MeetActivity;
import com.sparta.mixin.domain.meetactivity.entity.MeetActivityRepository;
import com.sparta.mixin.domain.meetannouncement.controller.MeetAnnouncementController;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetActivityService {
    private final MeetRepository meetRepository;
    private final MeetActivityRepository meetActivityRepository;
    private final ActivityParticipationRepository activityParticipationRepository;
    private final MeetAuthorizationService meetAuthorizationService;
    private static final Logger log = LoggerFactory.getLogger(MeetActivityService.class);

    /**
     * 모임 활동 리스트 조회
     *
     * @param meetId 모임 ID
     * @param user   현재 요청 중인 사용자
     * @return 활동 리스트 데이터
     */
    public List<MeetActivityListResponseDto> getActivitiesForMeet(Long meetId, User user) {
        log.info("모임 활동 리스트 조회 - 모임 ID: {}, 사용자: {}", meetId, user.getUsername());
        Meet meet = findMeetById(meetId);

        List<MeetActivity> activities = meetActivityRepository.findAllByMeet(meet);
        int totalMembers = meet.getTotalMembers();

        return activities.stream()
                .map(activity -> new MeetActivityListResponseDto(
                        activity,
                        totalMembers,
                        isUserParticipating(activity, user))
                )
                .collect(Collectors.toList());
    }

    /**
     * 모임 활동 상세 조회
     *
     * @param meetActivityId 활동 ID
     * @param user           현재 요청 중인 사용자
     * @return 활동 상세 데이터
     */
    public MeetActivityResponseDto getMeetActivityWithParticipants(Long meetActivityId, User user) {
        log.info("모임 활동 상세 조회 - 활동 ID: {}, 사용자: {}", meetActivityId, user.getUsername());
        MeetActivity meetActivity = findActivityById(meetActivityId);
        meetAuthorizationService.getUserRole(meetActivity.getMeet(), user);
        return new MeetActivityResponseDto(meetActivity);
    }

    /**
     * 새로운 모임 활동 생성
     *
     * @param meetId    모임 ID
     * @param requestDto 활동 생성 요청 데이터
     * @param user      현재 요청 중인 사용자
     */
    public void createMeetActivity(Long meetId, MeetActivityRequestDto requestDto, User user) {
        log.info("모임 활동 생성 - 모임 ID: {}, 사용자: {}", meetId, user.getUsername());
        Meet meet = findMeetById(meetId);
        checkLeaderOrSubleaderAuthorization(meet, user);

        MeetActivity meetActivity = MeetActivity.builder()
                .meet(meet)
                .title(requestDto.getTitle())
                .position(requestDto.getPosition())
                .date(requestDto.getDate())
                .content(requestDto.getContent())
                .build();

        meetActivityRepository.save(meetActivity);
        log.info("모임 활동 생성 완료 - 활동 제목: {}", requestDto.getTitle());
    }

    /**
     * 기존 모임 활동 수정
     *
     * @param meetActivityId 수정 대상 활동 ID
     * @param requestDto     활동 수정 요청 데이터
     * @param user           현재 요청 중인 사용자
     */
    public void updateMeetActivity(Long meetActivityId, MeetActivityRequestDto requestDto, User user) {
        log.info("모임 활동 수정 - 활동 ID: {}, 사용자: {}", meetActivityId, user.getUsername());
        MeetActivity meetActivity = findActivityById(meetActivityId);
        checkLeaderOrSubleaderAuthorization(meetActivity.getMeet(), user);

        meetActivity.updateActivity(requestDto);
        meetActivityRepository.save(meetActivity);
        log.info("모임 활동 수정 완료 - 활동 ID: {}", meetActivityId);
    }

    /**
     * 모임 활동 삭제
     *
     * @param meetActivityId 삭제 대상 활동 ID
     * @param user           현재 요청 중인 사용자
     */
    public void deleteMeetActivity(Long meetActivityId, User user) {
        log.info("모임 활동 삭제 - 활동 ID: {}, 사용자: {}", meetActivityId, user.getUsername());
        MeetActivity meetActivity = findActivityById(meetActivityId);
        checkLeaderOrSubleaderAuthorization(meetActivity.getMeet(), user);
        meetActivityRepository.delete(meetActivity);
        log.info("모임 활동 삭제 완료 - 활동 ID: {}", meetActivityId);
    }

    /**
     * 모임 활동 참여
     *
     * @param meetActivityId 참여할 활동 ID
     * @param user           현재 요청 중인 사용자
     */
    public void joinActivity(Long meetActivityId, User user) {
        log.info("모임 활동 참여 - 활동 ID: {}, 사용자: {}", meetActivityId, user.getUsername());
        MeetActivity meetActivity = findActivityById(meetActivityId);

        activityParticipationRepository.findByUserAndActivity(user, meetActivity)
                .ifPresent(participation -> {
                    throw new CustomException(ErrorCode.ALREADY_PARTICIPATING);
                });

        ActivityParticipation participation = ActivityParticipation.builder()
                .activity(meetActivity)
                .user(user)
                .build();

        activityParticipationRepository.save(participation);
        log.info("모임 활동 참여 완료 - 활동 ID: {}, 사용자: {}", meetActivityId, user.getUsername());
    }

    /**
     * 모임 활동 참여 취소
     *
     * @param meetActivityId 참여 취소할 활동 ID
     * @param user           현재 요청 중인 사용자
     */
    public void leaveActivity(Long meetActivityId, User user) {
        log.info("모임 활동 참여 취소 - 활동 ID: {}, 사용자: {}", meetActivityId, user.getUsername());
        MeetActivity meetActivity = findActivityById(meetActivityId);

        ActivityParticipation participation = activityParticipationRepository.findByUserAndActivity(user, meetActivity)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTICIPATION_NOT_FOUND));

        activityParticipationRepository.delete(participation);
        log.info("모임 활동 참여 취소 완료 - 활동 ID: {}, 사용자: {}", meetActivityId, user.getUsername());
    }

    /**
     * 모임 ID로 모임 엔티티 조회
     *
     * @param meetId 조회할 모임 ID
     * @return 조회된 모임 엔티티
     * @throws CustomException 모임이 존재하지 않을 경우
     */
    private Meet findMeetById(Long meetId) {
        return meetRepository.findById(meetId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEET_NOT_FOUND));
    }

    /**
     * 활동 ID로 활동 엔티티 조회
     *
     * @param activityId 조회할 활동 ID
     * @return 조회된 활동 엔티티
     * @throws CustomException 활동이 존재하지 않을 경우
     */
    private MeetActivity findActivityById(Long activityId) {
        return meetActivityRepository.findById(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }

    /**
     * 사용자가 활동에 참여하고 있는지 확인
     *
     * @param activity 확인할 활동 엔티티
     * @param user     확인할 사용자 엔티티
     * @return 사용자의 활동 참여 여부 (참여 중이면 true)
     */
    private boolean isUserParticipating(MeetActivity activity, User user) {
        return activity.getParticipants().stream()
                .anyMatch(participant -> participant.getUser().equals(user));
    }

    /**
     * 사용자 권한 확인 (리더 또는 서브 리더만 수행 가능)
     *
     * @param meet 모임 엔티티
     * @param user 확인할 사용자 엔티티
     * @throws CustomException 사용자가 리더 또는 서브 리더가 아닌 경우
     */
    private void checkLeaderOrSubleaderAuthorization(Meet meet, User user) {
        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
