package com.sparta.mixin.domain.meetannouncement.bookmark.entity;

import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementBookmarkRepository extends JpaRepository<AnnouncementBookmark, Long> {
    List<AnnouncementBookmark> findByUser(User user);
    Optional<AnnouncementBookmark> findByUserAndMeetAnnouncement(User user, MeetAnnouncement announcement);
}
