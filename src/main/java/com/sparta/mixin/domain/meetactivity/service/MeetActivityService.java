package com.sparta.mixin.domain.meetactivity.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityRequestDto;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityResponseDto;
import com.sparta.mixin.domain.meetactivity.entity.ActivityParticipation;
import com.sparta.mixin.domain.meetactivity.entity.ActivityParticipationRepository;
import com.sparta.mixin.domain.meetactivity.entity.MeetActivity;
import com.sparta.mixin.domain.meetactivity.entity.MeetActivityRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetActivityService {
    private final MeetActivityRepository meetActivityRepository;
    private final ActivityParticipationRepository activityParticipationRepository;
    private final MeetAuthorizationService meetAuthorizationService;
    private final MeetService meetService;



    /**
     * 모임 활동 조회 (참가자 포함)
     *
     * @param meetActivityId 활동 ID
     * @param user 현재 요청 중인 사용자
     * @return 활동 상세 정보와 참가자 목록
     * @throws CustomException 활동이 없거나 권한이 없는 경우 예외 발생
     */
    public MeetActivityResponseDto getMeetActivityWithParticipants(Long meetActivityId, User user) {
        MeetActivity meetActivity = meetActivityRepository.findById(meetActivityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));

        Meet meet = meetActivity.getMeet();
        if (meetAuthorizationService.getUserRole(meet, user) == null) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return new MeetActivityResponseDto(meetActivity);
    }

    /**
     * 모임 활동 생성
     *
     * @param meetId 모임 ID
     * @param requestDto 활동 요청 데이터
     * @param user 현재 요청 중인 사용자
     * @throws CustomException 권한이 없거나 모임이 없는 경우 예외 발생
     */
    public void createMeetActivity(Long meetId, MeetActivityRequestDto requestDto, User user) {
        Meet meet = meetService.findById(meetId);
        checkAuthorization(meet, user);

        MeetActivity meetActivity = MeetActivity.builder()
                .meet(meet)
                .title(requestDto.getTitle())
                .position(requestDto.getPosition())
                .date(requestDto.getDate())
                .content(requestDto.getContent())
                .build();

        meetActivityRepository.save(meetActivity);
    }

    /**
     * 모임 활동 수정
     *
     * @param meetActivityId 활동 ID
     * @param requestDto 수정 요청 데이터
     * @param user 현재 요청 중인 사용자
     * @throws CustomException 권한이 없거나 활동이 없는 경우 예외 발생
     */
    public void updateMeetActivity(Long meetActivityId, MeetActivityRequestDto requestDto, User user) {
        MeetActivity meetActivity = meetActivityRepository.findById(meetActivityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));

        checkAuthorization(meetActivity.getMeet(), user);
        meetActivity.updateActivity(requestDto);
        meetActivityRepository.save(meetActivity);
    }

    /**
     * 모임 활동 삭제
     *
     * @param meetActivityId 활동 ID
     * @param user 현재 요청 중인 사용자
     * @throws CustomException 권한이 없거나 활동이 없는 경우 예외 발생
     */
    public void deleteMeetActivity(Long meetActivityId, User user) {
        MeetActivity meetActivity = meetActivityRepository.findById(meetActivityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));

        checkAuthorization(meetActivity.getMeet(), user);
        meetActivityRepository.delete(meetActivity);
    }

    /**
     * 모임 활동 참여
     *
     * @param meetActivityId 활동 ID
     * @param user 현재 요청 중인 사용자
     * @throws CustomException 활동이 없거나 이미 참여 중인 경우 예외 발생
     */
    public void joinActivity(Long meetActivityId, User user) {
        MeetActivity meetActivity = meetActivityRepository.findById(meetActivityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));

        activityParticipationRepository.findByUserAndActivity(user, meetActivity)
                .ifPresent(participation -> { throw new CustomException(ErrorCode.ALREADY_PARTICIPATING); });

        ActivityParticipation participation = ActivityParticipation.builder()
                .activity(meetActivity)
                .user(user)
                .build();

        activityParticipationRepository.save(participation);
    }

    /**
     * 모임 활동 참여 취소
     *
     * @param meetActivityId 활동 ID
     * @param user 현재 요청 중인 사용자
     * @throws CustomException 활동이 없거나 참여하지 않은 경우 예외 발생
     */
    public void leaveActivity(Long meetActivityId, User user) {
        MeetActivity meetActivity = meetActivityRepository.findById(meetActivityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));

        ActivityParticipation participation = activityParticipationRepository.findByUserAndActivity(user, meetActivity)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTICIPATION_NOT_FOUND));

        activityParticipationRepository.delete(participation);
    }


    /**
     * 권한 확인
     *
     * @param meet 모임 엔티티
     * @param user 현재 요청 중인 사용자
     * @throws CustomException 권한이 없는 경우 FORBIDDEN 예외 발생
     */
    private void checkAuthorization(Meet meet, User user) {
        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
