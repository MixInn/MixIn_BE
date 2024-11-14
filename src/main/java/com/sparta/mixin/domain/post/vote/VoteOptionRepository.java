package com.sparta.mixin.domain.post.vote;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteOptionRepository extends JpaRepository<VoteOption,Long> {

    List<VoteOption> findAllByPostVote(PostVote postVote);
}
