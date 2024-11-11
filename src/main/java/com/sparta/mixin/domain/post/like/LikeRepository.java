package com.sparta.mixin.domain.post.like;

import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.like.entity.PostLike;
import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<PostLike, Long> {

    PostLike findByPostAndUser(Post post, User loginUser);
}
