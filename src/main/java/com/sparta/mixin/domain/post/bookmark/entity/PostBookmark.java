package com.sparta.mixin.domain.post.bookmark.entity;

import com.sparta.mixin.domain.post.CommunityType;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
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
@Table(name = "community_bookmark")
@RequiredArgsConstructor
public class PostBookmark extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "public_post_id")
    private PublicPost publicPost;

    @ManyToOne
    @JoinColumn(name = "meet_post_id")
    private MeetPost meetPost;

    @Enumerated(EnumType.STRING)
    private CommunityType communityType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public PostBookmark(PublicPost publicPost, User loginUser) {
        this.publicPost = publicPost;
        this.communityType = CommunityType.PUBLICPOST;
        this.user = loginUser;
    }

    public PostBookmark(MeetPost meetPost, User loginUser) {
        this.meetPost = meetPost;
        this.communityType = CommunityType.MEETPOST;
        this.user = loginUser;
    }
}