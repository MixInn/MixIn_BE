package com.sparta.mixin.domain.community.meetpost;

import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetPostRepository extends JpaRepository<MeetPost,Long> {

}
