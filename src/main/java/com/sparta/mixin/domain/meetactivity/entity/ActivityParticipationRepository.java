package com.sparta.mixin.domain.meetactivity.entity;

import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityParticipationRepository extends JpaRepository<ActivityParticipation, Long> {
    Optional<ActivityParticipation> findByUserAndActivity(User user, MeetActivity activity);
}
