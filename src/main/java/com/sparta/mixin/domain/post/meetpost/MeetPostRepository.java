package com.sparta.mixin.domain.post.meetpost;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.post.entity.MeetPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetPostRepository extends JpaRepository<MeetPost, Long> {

    Page<MeetPost> findAllByMeet(Meet meet, Pageable pageable);

    Page<MeetPost> findAllByUser_UniversityAndPostTypeAndMeet(String university, String postType, Meet meet, Pageable pageable);

    Page<MeetPost> findAllByUser_UniversityAndPostTypeAndMeetOrderByClickCountDesc(String university, String postType,
        Meet meet, Pageable pageable);

    Page<MeetPost> findAllByUser_UniversityAndPostTypeAndMeetOrderByLikeCountDesc(String university, String postType,
        Meet meet, Pageable pageable);
}
