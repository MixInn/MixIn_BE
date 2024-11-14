package com.sparta.mixin.domain.post.vote;

import com.sparta.mixin.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostVoteRepository extends JpaRepository<PostVote,Long> {

    List<PostVote> findAllByPost(Post post);
}
