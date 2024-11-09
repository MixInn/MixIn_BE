package com.sparta.mixin.domain.community.like;

import com.sparta.mixin.domain.community.like.entity.CommunityLike;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<CommunityLike, Long> {

    CommunityLike findByPublicPostAndUser(PublicPost publicPost, User loginUser);

    CommunityLike findByMeetPostAndUser(MeetPost meetPost, User loginUser);
}
