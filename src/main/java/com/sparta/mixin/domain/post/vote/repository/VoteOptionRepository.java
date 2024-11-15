package com.sparta.mixin.domain.post.vote.repository;

import com.sparta.mixin.domain.post.vote.entity.PostVote;
import com.sparta.mixin.domain.post.vote.entity.VoteOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteOptionRepository extends JpaRepository<VoteOption,Long> {

    List<VoteOption> findAllByPostVote(PostVote postVote);

    void deleteAllByPostVote(PostVote postVote);
}
