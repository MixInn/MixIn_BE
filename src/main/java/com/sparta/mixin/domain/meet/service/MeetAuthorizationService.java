package com.sparta.mixin.domain.meet.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetAuthorization;
import com.sparta.mixin.domain.meet.entity.MeetAuthorizationRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetAuthorizationService {
    private final MeetAuthorizationRepository meetAuthorizationRepository;
    private static final Logger log = LoggerFactory.getLogger(MeetAuthorizationService.class);

    /**
     * MeetAuthorization을 저장하는 메서드
     * @param meetAuthorization 저장할 MeetAuthorization 객체
     * @return 저장된 MeetAuthorization 객체
     */
    public MeetAuthorization save(MeetAuthorization meetAuthorization) {
        log.info("모임 권한 저장 - 모임 ID: {}", meetAuthorization.getMeet().getId());
        return meetAuthorizationRepository.save(meetAuthorization);
    }


    /**
     * 사용자의 모임 역할을 조회하는 메서드
     * @param meet 조회할 Meet 객체
     * @param user 역할을 조회할 User 객체
     * @return 사용자의 AuthorizationLevel
     */
    public AuthorizationLevel getUserRole(Meet meet, User user) {
        log.debug("모임 권한 조회 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meet.getId());
        return meetAuthorizationRepository.findByMeetAndUser(meet, user)
                .map(MeetAuthorization::getAuthorization)
                .orElseThrow(() -> {
                    log.error("권한 없음 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meet.getId());
                    return new CustomException(ErrorCode.UNAUTHORIZED_USER);
                });
    }

    /**
     * 모임에 속한 모든 사용자 목록을 반환하는 메서드
     * @param meetId 모임 ID
     * @return 모임에 속한 모든 사용자 목록
     */
    public List<User> getUsersByMeetId(Long meetId) {
        log.debug("모임 사용자 조회 - 모임 ID: {}", meetId); // 간결한 디버그 로깅
        Meet meet = new Meet(); // meetId로부터 meet 객체를 가져오는 코드가 필요합니다.
        List<MeetAuthorization> meetAuthorizations = meetAuthorizationRepository.findByMeet(meet);

        // 사용자 목록 반환
        return meetAuthorizations.stream()
                .map(MeetAuthorization::getUser) // MeetAuthorization에서 User 추출
                .collect(Collectors.toList());
    }

    /**
     * 특정 모임과 사용자의 권한 정보를 조회하는 메서드
     * @param meet 조회할 Meet 객체
     * @param user 조회할 User 객체
     * @return 해당 모임과 사용자의 MeetAuthorization 정보
     */
    public Optional<MeetAuthorization> findByMeetAndUser(Meet meet, User user) {
        log.debug("모임 사용자 권한 조회 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meet.getId());
        return meetAuthorizationRepository.findByMeetAndUser(meet, user);
    }

    /**
     * 사용자가 모임의 구성원인지 확인하는 메서드
     * @param meet 확인할 Meet 객체
     * @param user 확인할 User 객체
     * @return 사용자 여부 (true: 구성원, false: 비구성원)
     */
    public boolean isUserMemberOfMeet(Meet meet, User user) {
        log.debug("구성원 확인 - 사용자 ID: {}, 모임 ID: {}", user.getId(), meet.getId());
        return meetAuthorizationRepository.existsByMeetAndUser(meet, user);
    }

    /**
     * 특정 MeetAuthorization을 삭제하는 메서드
     * @param meetAuthorization 삭제할 MeetAuthorization 객체
     */
    public void deleteMeetAuthorization(MeetAuthorization meetAuthorization) {
        log.info("모임 권한 삭제 - 모임 ID: {}", meetAuthorization.getMeet().getId()); // 간결한 로깅
        meetAuthorizationRepository.delete(meetAuthorization);
    }

    /**
     * 특정 모임의 리더를 조회하는 메서드
     * @param meetId 모임 ID
     * @return 모임 리더(User)
     */
    public User getMeetLeader(Long meetId) {
        log.debug("모임 리더 조회 - 모임 ID: {}", meetId);
        return meetAuthorizationRepository.findByMeetIdAndAuthorization(meetId, AuthorizationLevel.LEADER)
                .map(MeetAuthorization::getUser)
                .orElseThrow(() -> {
                    log.error("모임 리더를 찾을 수 없습니다 - 모임 ID: {}", meetId);
                    return new CustomException(ErrorCode.NOT_FOUND_USER);
                });
    }
}
