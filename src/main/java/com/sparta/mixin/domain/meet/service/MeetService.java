package com.sparta.mixin.domain.meet.service;

import com.sparta.mixin.domain.meet.dto.MeetRequestDto;
import com.sparta.mixin.domain.meet.entity.*;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncementService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetService {
    private final MeetRepository meetRepository;
    private final MeetAuthorizationService meetAuthorizationService;


    public Meet createMeet(MeetRequestDto requestDto, User user) {
        MeetType meetType = MeetType.fromString(requestDto.getType());
        MeetCategory meetCategory = MeetCategory.fromString(requestDto.getCategory());

        Meet meet = Meet.builder()
                .type(meetType)
                .meetCategory(meetCategory)
                .image(requestDto.getImage())
                .name(requestDto.getName())
                .info(requestDto.getInfo())
                .rule(requestDto.getRule())
                .tag(requestDto.getTag())
                .build();
        // 밑 생성
        Meet savedMeet = meetRepository.save(meet);



        // 유저를 생성자 권한으로 추가하기
        MeetAuthorization meetAuthorization = MeetAuthorization.builder()
                .meet(meet)
                .user(user)
                .authorization(AuthorizationLevel.LEADER)
                .build();

        meetAuthorizationService.save(meetAuthorization);

        return savedMeet;
    }



    public void updateMeet(Long meetId, MeetRequestDto requestDto, User user) {
        // 유저 권한 확인
        Meet meet = findById(meetId);


        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);

        // 사용자 권한 확인 ( 리더 or 부리더인 경우 수정 가능 )
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 존재한다
        meet.updateMeet(requestDto);

    }

    public void deleteMeet(Long meetId, User user) {
        // 유저 권한 확인
        Meet meet = meetRepository.findById(meetId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));


        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, user);

        // 사용자 권한 확인 ( 리더인 경우 삭제 가능 )
        if (userRole != AuthorizationLevel.LEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        meetRepository.deleteById(meetId);
    }

    public Meet findById(Long meetId) {
        return meetRepository.findById(meetId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
