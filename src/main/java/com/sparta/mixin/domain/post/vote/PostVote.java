package com.sparta.mixin.domain.post.vote;

import com.sparta.mixin.domain.post.entity.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "postVote",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<VoteOption> voteOptions;

    private LocalDateTime deadline;

    private boolean isAnonymous;

    private boolean allowMultipleVotes;

    public <T extends Post> PostVote(VoteRequestDto voteRequestDto, T post) {
        this.deadline=voteRequestDto.getDeadline();
        this.isAnonymous=voteRequestDto.isAnonymous();
        this.allowMultipleVotes=voteRequestDto.isAllowMultipleVotes();
        this.post=post;
    }

    public void updateVote(VoteRequestDto voteRequestDto) {
        this.deadline=voteRequestDto.getDeadline();
        this.isAnonymous=voteRequestDto.isAnonymous();
        this.allowMultipleVotes=voteRequestDto.isAllowMultipleVotes();
    }
}
