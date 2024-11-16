package com.sparta.mixin.domain.meetactivity.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetActivityRepository extends JpaRepository<MeetActivity, Long> {
    List<MeetActivity> findAllByMeet(Meet meet);
}
