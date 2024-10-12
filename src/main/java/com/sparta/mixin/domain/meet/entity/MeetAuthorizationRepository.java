package com.sparta.mixin.domain.meet.entity;

import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetAuthorizationRepository extends JpaRepository<MeetAuthorization, Long> {
    // 특정 모임(meet)에 속한 모든 사용자 목록 가져오기
    List<MeetAuthorization> findByMeet(Meet meet);

    Optional<MeetAuthorization> findByMeetAndUser(Meet meet, User user);
}
