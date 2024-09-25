package com.sparta.mixin.domain.community.comment.entity;

import com.sparta.mixin.domain.community.CommunityType;
import com.sparta.mixin.domain.community.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "community_comment")
@RequiredArgsConstructor
public class CommunityComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publicPost_id")
    private PublicPost publicPost;

    @ManyToOne
    @JoinColumn(name = "meetPost_id")
    private MeetPost meetPost;

    @Enumerated(EnumType.STRING)
    private CommunityType communityType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String comment;

    public CommunityComment(PublicPost publicPost, CommentRequestDto commentRequestDto) {
        this.comment= commentRequestDto.getComment();
        this.publicPost=publicPost;
        this.communityType=CommunityType.PUBLICPOST;

    }

    public CommunityComment(MeetPost meetPost, CommentRequestDto commentRequestDto) {
        this.comment= commentRequestDto.getComment();
        this.meetPost=meetPost;
        this.communityType=CommunityType.MEETPOST;
    }

    // Getters and setters
}
