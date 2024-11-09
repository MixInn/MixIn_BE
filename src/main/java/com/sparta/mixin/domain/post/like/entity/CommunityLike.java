package com.sparta.mixin.domain.community.like.entity;

import com.sparta.mixin.domain.community.CommunityType;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "community_like")
@RequiredArgsConstructor
public class CommunityLike extends Timestamped {

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


    public CommunityLike(PublicPost publicPost, User loginUser) {
        this.publicPost = publicPost;
        this.communityType = CommunityType.PUBLICPOST;
        this.user = loginUser;
    }

    public CommunityLike(MeetPost meetPost, User loginUser) {
        this.meetPost = meetPost;
        this.communityType = CommunityType.MEETPOST;
        this.user = loginUser;
    }
}
