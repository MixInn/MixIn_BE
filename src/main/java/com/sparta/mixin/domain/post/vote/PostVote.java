package com.sparta.mixin.domain.post.vote;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "post_vote")
@RequiredArgsConstructor
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "postVote",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<VoteOption> voteOptions=new ArrayList<>();

    private LocalDateTime deadline;

    private boolean isAnonymous;

    private boolean allowMultipleVotes;

    public PostVote(VoteRequestDto voteRequestDto) {
        this.deadline=voteRequestDto.getDeadline();
        this.isAnonymous=voteRequestDto.isAnonymous();
        this.allowMultipleVotes=voteRequestDto.isAllowMultipleVotes();
        for (String voteOption : voteRequestDto.getVoteOption()) {
            VoteOption newVoteOption = new VoteOption(voteOption,this);
            voteOptions.add(newVoteOption);
        }
    }
}
