package com.sparta.mixin.domain.post.vote.repository;

import com.sparta.mixin.domain.post.vote.entity.PostVote;
import com.sparta.mixin.domain.post.vote.entity.VoteOption;
import com.sparta.mixin.domain.post.vote.entity.VoteResult;
import com.sparta.mixin.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteResultRepository extends JpaRepository<VoteResult,Long> {

    List<User> findUserByVoteOption(VoteOption voteOption);

    Long countByVoteOption(VoteOption voteOption);

    List<VoteResult> findAllByPostVoteAndUser(PostVote postVote, User loginUser);
}
