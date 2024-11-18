package com.sparta.mixin.domain.post.vote.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "vote_option")
@RequiredArgsConstructor
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private PostVote postVote;

    private String optionText;

    @OneToMany(mappedBy = "voteOption",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<VoteResult> voteResults;

    public VoteOption(PostVote postVote, String optionText) {
        this.postVote=postVote;
        this.optionText=optionText;
    }
}
