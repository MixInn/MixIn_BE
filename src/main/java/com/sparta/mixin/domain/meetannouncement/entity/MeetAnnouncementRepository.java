package com.sparta.mixin.domain.meetannouncement.entity;

import com.sparta.mixin.domain.meet.entity.MeetCategory;
import com.sparta.mixin.domain.meet.entity.MeetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MeetAnnouncementRepository extends JpaRepository<MeetAnnouncement, Long> {

    @Query("SELECT a FROM MeetAnnouncement a WHERE " +
            "(:meetType IS NULL OR a.meet.type = :meetType) AND " +
            "(:category IS NULL OR a.meet.category = :category) AND " +
            "(:tags IS NULL OR :tags = '' OR a.preferences LIKE %:tags%) AND " +
            "(:meetName IS NULL OR :meetName = '' OR a.meet.name LIKE %:meetName%) AND " +
            "(:university IS NULL OR a.university = :university)")
    Page<MeetAnnouncement> findAnnouncementsWithFilters(
            @Param("meetType") MeetType meetType,
            @Param("category") MeetCategory category,
            @Param("tags") String tags,
            @Param("meetName") String meetName,
            @Param("university") String university,  // 추가
            Pageable pageable);


    Optional<MeetAnnouncement> findByMeetId(Long meetId);
}
