package com.sparta.mixin.domain.meetannouncement.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetAnnouncementRepository extends JpaRepository<MeetAnnouncement, Long> {

    Optional<MeetAnnouncement> findByMeetId(Long meetId);
}
