package com.sparta.mixin.domain.community.meetcommunity.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "meetCommunity")
public class MeetCommunity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    private String title;
    private String content;

    // Getters and setters
}
