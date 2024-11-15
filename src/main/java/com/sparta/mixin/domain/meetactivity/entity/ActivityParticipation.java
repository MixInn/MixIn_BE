package com.sparta.mixin.domain.meetactivity.entity;

import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_participation")
@Getter
@NoArgsConstructor
public class ActivityParticipation extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private MeetActivity activity;


    // Getters and setters
    @Builder
    public ActivityParticipation(User user, MeetActivity activity) {
        this.user = user;
        this.activity = activity;
    }
}

