package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.post.vote.dto.VoteRequestDto;
import lombok.Getter;

@Getter
public class PostRequestDto {

    private String title;
    private String content;
    private VoteRequestDto voteRequestDto;

}
