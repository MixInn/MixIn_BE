package com.sparta.mixin.domain.post.like;

import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.post.like.entity.PostLike;
import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<PostLike, Long> {

    PostLike findByPublicPostAndUser(PublicPost publicPost, User loginUser);

    PostLike findByMeetPostAndUser(MeetPost meetPost, User loginUser);
}
