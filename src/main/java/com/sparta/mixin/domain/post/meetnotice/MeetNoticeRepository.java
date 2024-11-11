package com.sparta.mixin.domain.post.meetnotice;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetNoticeRepository extends JpaRepository<MeetNotice, Long> {

    Page<MeetNotice> findAllByUser_UniversityAndPostTypeAndMeet(String university, String postType, Meet meet, Pageable pageable);
}
