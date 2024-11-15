package com.sparta.mixin.domain.post.vote.dto;

import com.sparta.mixin.domain.post.vote.entity.PostVote;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class VoteResponseDto {
    private Long id;
    private List<String> voteOptions;
    private LocalDateTime deadline;
    private boolean isAnonymous;
    private boolean allowMultipleVotes;

    public VoteResponseDto(PostVote postVote,List<String> optionTextList) {
        this.id = postVote.getId();
        this.voteOptions = optionTextList;
        this.deadline = postVote.getDeadline();
        this.isAnonymous = postVote.isAnonymous();
        this.allowMultipleVotes = postVote.isAllowMultipleVotes();
    }
}
