package com.sparta.mixin.domain.meetapplication.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetApplicationRepository extends JpaRepository<MeetApplication, Long> {
    MeetApplication findByMeet(Meet meet);
}
