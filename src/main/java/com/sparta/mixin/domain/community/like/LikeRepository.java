package com.sparta.mixin.domain.community.like;

import com.sparta.mixin.domain.community.like.entity.CommunityLike;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<CommunityLike,Long> {

    CommunityLike findByPublicPost(PublicPost publicPost);

    CommunityLike findByMeetPost(MeetPost meetPost);
}
