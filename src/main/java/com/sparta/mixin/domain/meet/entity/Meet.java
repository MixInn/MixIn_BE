package com.sparta.mixin.domain.meet.entity;

import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Meet")
public class Meet extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MeetType type;

    @Enumerated(EnumType.STRING)
    private MeetCategory category;

    private String image;
    private String name;
    private String info;
    private String rule;
    private String tag;

    // Getters and setters
}