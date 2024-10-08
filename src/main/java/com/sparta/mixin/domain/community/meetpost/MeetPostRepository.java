package com.sparta.mixin.domain.community.meetpost;

import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.meet.entity.Meet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetPostRepository extends JpaRepository<MeetPost,Long> {

    Page<MeetPost> findAllByMeet(Meet meet, Pageable pageable);
}
