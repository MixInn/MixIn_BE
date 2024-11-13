package com.sparta.mixin.domain.meetapplication.entity;

import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetApplicationRepository extends JpaRepository<MeetApplication, Long> {
    List<MeetApplication> findByUser(User user);
    List<MeetApplication> findByMeetAnnouncement(MeetAnnouncement meetAnnouncement);
    boolean existsByMeetAnnouncementAndUser(MeetAnnouncement meetAnnouncement, User user);
}
