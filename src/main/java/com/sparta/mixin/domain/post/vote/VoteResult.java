package com.sparta.mixin.domain.post.vote;

import com.sparta.mixin.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "vote_result")
@RequiredArgsConstructor
public class VoteResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vote_option_id")
    private VoteOption voteOption;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    private LocalDateTime voteTime;

    public VoteResult(VoteOption voteOption, User user, LocalDateTime voteTime) {
        this.voteOption = voteOption;
        this.user = user; // 익명 투표의 경우 null
        this.voteTime = voteTime;
    }
}
