package com.sparta.mixin.domain.meetactivity.entity;

import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "activity_participation")
public class ActivityParticipation extends Timestamped {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private MeetActivity activity;

    // Getters and setters
}

