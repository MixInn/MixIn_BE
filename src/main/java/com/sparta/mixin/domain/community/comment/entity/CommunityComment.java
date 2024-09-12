package com.sparta.mixin.domain.community.comment.entity;

import com.sparta.mixin.domain.community.CommunityType;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "communityComment")
public class CommunityComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private MeetPost community;

    @Enumerated(EnumType.STRING)
    private CommunityType communityType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    // Getters and setters
}
