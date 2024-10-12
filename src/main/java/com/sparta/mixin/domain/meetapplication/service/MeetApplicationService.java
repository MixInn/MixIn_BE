package com.sparta.mixin.domain.meetapplication.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetAuthorization;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationRequestDto;
import com.sparta.mixin.domain.meetapplication.entity.MeetApplication;
import com.sparta.mixin.domain.meetapplication.entity.MeetApplicationRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetApplicationService {
    private final MeetApplicationRepository meetApplicationRepository;
    private final MeetService meetService;
    private final MeetAuthorizationService meetAuthorizationService;

    public void createMeetApplication(MeetApplicationRequestDto requestDto) {
        // 모임 지원서
        Meet meet = meetService.findById(requestDto.getMeetId());

        User user = new User();

        MeetApplication meetApplication = MeetApplication.builder()
                .meet(meet)
                .user(user)
                .content(requestDto.getContent())
                .build();

        meetApplicationRepository.save(meetApplication);
    }

    public void updateMeetApplication(MeetApplicationRequestDto requestDto) {
        // 지원서에서 수정할 수 있는 내용은 지원 내용밖에 없음
        Meet meet = meetService.findById(requestDto.getMeetId());

        User user = new User();

        MeetApplication meetApplication = meetApplicationRepository.findByMeet(meet);

        // 모임 지원서 수정
        meetApplication.updateContent(requestDto.getContent());
    }



    public void deleteMeetApplication(Long meetApplicationId,User user) {

        MeetApplication meetApplication = getMeetApplication(meetApplicationId);

        meetApplicationRepository.delete(meetApplication);
    }

    public void acceptMeetApplication(Long meetApplicationId) {
        MeetApplication meetApplication = getMeetApplication(meetApplicationId);

        // 수락 상태로 변경
        meetApplication.acceptApplication();

        // 신청이 수락되면 MeetAuthorization에 추가
        Meet meet = meetApplication.getMeet();
        User user = meetApplication.getUser();

        // 기본 권한을 MEMBER로 설정 (필요에 따라 LEADER 등으로 설정할 수 있음)
        MeetAuthorization meetAuthorization = MeetAuthorization.builder()
                .meet(meet)
                .user(user)
                .authorization(AuthorizationLevel.MEMBER)  // 기본적으로 MEMBER 권한 부여
                .build();

        // MeetAuthorization 저장
        meetAuthorizationService.save(meetAuthorization);


        // 업데이트된 상태 저장
        meetApplicationRepository.save(meetApplication);
    }

    public void rejectMeetApplication(Long meetApplicationId,String reason) {
        MeetApplication meetApplication = getMeetApplication(meetApplicationId);

        // 거절 상태로 변경
        meetApplication.rejectApplication(reason);

        // 업데이트된 상태 저장
        meetApplicationRepository.save(meetApplication);
    }

    public MeetApplication getMeetApplication(Long meetApplicationId) {
        return meetApplicationRepository.findById(meetApplicationId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
