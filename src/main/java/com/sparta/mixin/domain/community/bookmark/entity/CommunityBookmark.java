package com.sparta.mixin.domain.community.bookmark.entity;

import com.sparta.mixin.domain.community.CommunityType;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "communityBookmark")
public class CommunityBookmark extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "communoty_id")
    private MeetPost community;

    @Enumerated(EnumType.STRING)
    private CommunityType communityType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    // Getters and setters
}