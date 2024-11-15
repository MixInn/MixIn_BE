package com.sparta.mixin.domain.post.vote.dto;

import com.sparta.mixin.domain.post.vote.entity.VoteOption;
import java.util.List;
import lombok.Getter;

@Getter
public class VoteOptionResponseDto {
    private Long id;
    private String voteOptionText;
    private Long voteCount;
    private List<String> usernames;

    public VoteOptionResponseDto(VoteOption voteOption, Long voteCount, List<String> voteUser) {
        this.id=voteOption.getId();
        this.voteOptionText=voteOption.getOptionText();
        this.voteCount=voteCount;
        this.usernames=voteUser;
    }
}
