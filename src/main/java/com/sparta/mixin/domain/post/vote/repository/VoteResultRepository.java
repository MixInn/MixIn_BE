package com.sparta.mixin.domain.post.vote.repository;

import com.sparta.mixin.domain.post.vote.entity.PostVote;
import com.sparta.mixin.domain.post.vote.entity.VoteOption;
import com.sparta.mixin.domain.post.vote.entity.VoteResult;
import com.sparta.mixin.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteResultRepository extends JpaRepository<VoteResult,Long> {

    @Query("SELECT vr.user FROM VoteResult vr WHERE vr.voteOption=:voteOption")
    List<User> findUserByVoteOption(@Param("voteOption") VoteOption voteOption);

    Long countByVoteOption(VoteOption voteOption);

    List<VoteResult> findAllByPostVoteAndUser(PostVote postVote, User loginUser);
}
