package com.sparta.mixin.domain.image.entity;

import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "entityMappingImage")
public class EntityMappingImage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    private Long entityId;

    @ManyToOne
    @JoinColumn(name = "publicCommunity_id", nullable = false)
    private PublicPost publicPost;

    @ManyToOne
    @JoinColumn(name = "meetCommunity_id", nullable = false)
    private MeetPost meetPost;

    // Getters and setters
}