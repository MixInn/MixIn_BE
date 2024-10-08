package com.sparta.mixin.domain.meetnotice.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "meetNotice")
public class MeetNotice extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    // Getters and setters
}

