package com.sparta.mixin.domain.meet.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetAuthorization;
import com.sparta.mixin.domain.meet.entity.MeetAuthorizationRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetAuthorizationService {
    private final MeetAuthorizationRepository meetAuthorizationRepository;

    public void save(MeetAuthorization meetAuthorization) {
        meetAuthorizationRepository.save(meetAuthorization);
    }

    public AuthorizationLevel getUserRole(Meet meet, User user) {
        return meetAuthorizationRepository.findByMeetAndUser(meet, user)
                .map(MeetAuthorization::getAuthorization)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    // 특정 모임에 속한 모든 사용자 목록을 반환하는 메소드 추가
    public List<User> getUsersByMeetId(Long meetId) {
        Meet meet = new Meet(); // meetId로부터 meet 객체를 가져오는 코드가 필요합니다.
        List<MeetAuthorization> meetAuthorizations = meetAuthorizationRepository.findByMeet(meet);

        // 사용자 목록을 반환
        return meetAuthorizations.stream()
                .map(MeetAuthorization::getUser) // 각 MeetAuthorization에서 User를 추출
                .collect(Collectors.toList());
    }
}
