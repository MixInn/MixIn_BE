package com.sparta.mixin.domain.meet.service;

import com.sparta.mixin.domain.meet.dto.MeetRequestDto;
import com.sparta.mixin.domain.meet.entity.*;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetService {
    private final MeetRepository meetRepository;
    private final MeetAuthorizationService meetAuthorizationService;


    public void createMeet(MeetRequestDto requestDto) {
        // 밑 타입 string -> enum으로 변환
        MeetType meetType = MeetType.fromString(requestDto.getType());
        // 밑 카테고리도 enum으로 변환
        MeetCategory meetCategory = MeetCategory.fromString(requestDto.getCategory());
        // 유저 확인 추가

        //todo 사진 저장하는 함수 추가해야함
        String imageUrl = "";

        Meet meet = Meet.builder()
                .type(meetType)
                .meetCategory(meetCategory)
                .image(imageUrl)
                .name(requestDto.getName())
                .info(requestDto.getInfo())
                .rule(requestDto.getRule())
                .tag(requestDto.getTag())
                .build();
        // 밑 생성
        meetRepository.save(meet);

        // 임시 유저
        User user = new User();

        // 유저를 생성자 권한으로 추가하기
        MeetAuthorization meetAuthorization = MeetAuthorization.builder()
                .meet(meet)
                .user(user)
                .authorization(AuthorizationLevel.LEADER)
                .build();

        meetAuthorizationService.save(meetAuthorization);
    }



    public void updateMeet(Long meetId, MeetRequestDto requestDto) {
        // 유저 권한 확인
        Meet meet = findMeetById(meetId);

        // 임시 유저
        User currentUser = new User();

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, currentUser);

        // 사용자 권한 확인 ( 리더 or 부리더인 경우 수정 가능 )
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 존재한다
        meet.updateMeet(requestDto);

    }

    public void deleteMeet(Long meetId) {
        // 유저 권한 확인
        Meet meet = meetRepository.findById(meetId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 임시 유저
        User currentUser = new User();

        AuthorizationLevel userRole = meetAuthorizationService.getUserRole(meet, currentUser);

        // 사용자 권한 확인 ( 리더인 경우 삭제 가능 )
        if (userRole != AuthorizationLevel.LEADER && userRole != AuthorizationLevel.SUBLEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        meetRepository.deleteById(meetId);
    }

    public Meet findMeetById(Long meetId) {
        return meetRepository.findById(meetId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
