package com.sparta.mixin.domain.post.vote.dto;

import com.sparta.mixin.domain.post.vote.entity.PostVote;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class VoteResponseDto<T> {
    private Long id;
    private List<T> voteOptions;
    private LocalDateTime deadline;
    private boolean isAnonymous;
    private boolean allowMultipleVotes;

    private VoteResponseDto(PostVote postVote,List<T> voteOptions) {
        this.id = postVote.getId();
        this.voteOptions = voteOptions;
        this.deadline = postVote.getDeadline();
        this.isAnonymous = postVote.isAnonymous();
        this.allowMultipleVotes = postVote.isAllowMultipleVotes();
    }

    // 팩토리 메서드
    public static VoteResponseDto fromVoteResponse(PostVote postVote,List<String> optionTextList){
        return new VoteResponseDto(postVote,optionTextList);
    }

    public static VoteResponseDto  fromVoteResultResponse(PostVote postVote,List<VoteOptionResponseDto> voteOptionResponseDtos){
        return new VoteResponseDto(postVote,voteOptionResponseDtos);
    }
}
