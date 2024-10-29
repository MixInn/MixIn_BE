package com.sparta.mixin.domain.meetactivity.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityRequestDto;
import com.sparta.mixin.domain.meetactivity.entity.MeetActivity;
import com.sparta.mixin.domain.meetactivity.entity.MeetActivityRepository;
import com.sparta.mixin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetActivityService {
    private final MeetActivityRepository meetActivityRepository;


    private final MeetAuthorizationService meetAuthorizationService;
    private final MeetService meetService;

    // 모임 활동 생성
    public void createMeetActivity(Long meetId, MeetActivityRequestDto requestDto, User user) {
        // 권한 확인 (리더, 부리더만 생성 가능)
        Meet meet = meetService.findById(meetId); // 해당 모임 가져오기
        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);

        // 권한이 리더 또는 부리더인지 확인
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 권한이 확인된 경우 모임 활동 생성
        MeetActivity meetActivity = MeetActivity.builder()
                .meet(meet)
                .activityPosition(requestDto.getActivityPosition())
                .date(requestDto.getDate())
                .content(requestDto.getContent())
                .build();

        meetActivityRepository.save(meetActivity);
    }

    // 모임 활동 수정
    public void updateMeetActivity(Long meetActivityId, MeetActivityRequestDto requestDto, User user) {
        // 권한 확인 (리더, 부리더만 수정 가능)
        MeetActivity meetActivity = meetActivityRepository.findById(meetActivityId)
                .orElseThrow(() -> new IllegalArgumentException("모임 활동을 찾을 수 없습니다."));

        Meet meet = meetActivity.getMeet();
        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);

        // 권한이 리더 또는 부리더인지 확인
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 권한이 확인된 경우 모임 활동 수정
        meetActivity.updateActivity(requestDto);
        meetActivityRepository.save(meetActivity);
    }

    // 모임 활동 삭제
    public void deleteMeetActivity(Long meetActivityId, User user) {
        // 권한 확인 (리더, 부리더만 삭제 가능)
        MeetActivity meetActivity = meetActivityRepository.findById(meetActivityId)
                .orElseThrow(() -> new IllegalArgumentException("모임 활동을 찾을 수 없습니다."));

        Meet meet = meetActivity.getMeet();
        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);

        // 권한이 리더 또는 부리더인지 확인
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 권한이 확인된 경우 모임 활동 삭제
        meetActivityRepository.delete(meetActivity);
    }
}
