package com.sparta.mixin.domain.post.vote.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class VoteRequestDto {

    private List<String> voteOption; // 투표항목
    private LocalDateTime deadline;  // 마감날짜
    private boolean isAnonymous;  // 익명투표 유무
    private boolean allowMultipleVotes;  // 중복투표 유무

}
