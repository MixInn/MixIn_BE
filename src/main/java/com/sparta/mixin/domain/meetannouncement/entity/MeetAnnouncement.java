package com.sparta.mixin.domain.meetannouncement.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MeetAnnouncement")
public class MeetAnnouncement extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    @Enumerated(EnumType.STRING)
    private AnnouncementType type;

    @Enumerated(EnumType.STRING)
    private AnnouncementCategory category;

    private String image;
    private String name;
    private String info;
    private String rule;
    private String tag;


    // Getters and setters
}