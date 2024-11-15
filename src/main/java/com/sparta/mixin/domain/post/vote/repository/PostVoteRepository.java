package com.sparta.mixin.domain.post.vote.repository;

import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.vote.entity.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostVoteRepository extends JpaRepository<PostVote,Long> {

    PostVote findByPost(Post post);
}
